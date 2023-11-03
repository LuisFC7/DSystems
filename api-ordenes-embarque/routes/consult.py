import datetime
import random

from fastapi import HTTPException
from fastapi.routing import APIRouter
from db.connection import init_db, close_connection
from loguru import logger as logging
# from models.Crm import ClientCrm as Client
# from models.OrdenCompra import PurchaseOrder
# from models.Crm import Quotation
# from models.Crm import ClientCrm


# llamar instancia
from dto.ShipmentOrder import ShipmentOrder, order, objectList
from dto.OrdenesCompra import OrdenesCompra, listOrdenCompra
from dto.Cotizacion import Cotizacion, cotizacion
from models.OrdenEmbarque import ShippingOrder
from models.views.VOrdenEmbarque import VShippingOrder
from models.views.VOrdenesCompraAuth import VPurchaseOrdersAuth

router = APIRouter()


@router.get("/test")
async def test():
    return {"message": "test in route consult"}


@router.get("/purchase_orders_auth")
async def get_purchase_orders_auth(block: int = 1, limit: int = 300):
    try:
        await init_db()
    except Exception as e:
        logging.error(e)
        await close_connection()
        raise HTTPException(status_code=500, detail="Error in init db")

    if block <= 0:
        raise HTTPException(status_code=400, detail="Block must be greater than 0")

    if limit <= 0:
        raise HTTPException(status_code=400, detail="Limit must be greater than 0")

    try:

        orders = await (VPurchaseOrdersAuth.all().order_by("-authDate").limit(limit).offset((block - 1) * limit)
                        .values("orderNumber", "endValidityDate", "client", "originCity", "destinationCity",
                                "serviceType", "hazmat", "alliance", "authDate"))
        await close_connection()
        return {"message": "Consult Success", "data": orders}

    except Exception as e:
        logging.error(e)
        await close_connection()
        raise HTTPException(status_code=500, detail="Error in query Ordenes de Compra")


@router.get("/shipmentOrders")
async def get_orders(block: int = 1, limit: int = 300):

    if block <= 0:
        raise HTTPException(status_code=400, detail="Block must be greater than 0")

    if limit <= 0:
        raise HTTPException(status_code=400, detail="Limit must be greater than 0")

    try:
        await init_db()
    except Exception as e:
        logging.error(e)
        await close_connection()
        raise HTTPException(status_code=500, detail="Error in init db")

    try:
        # result = await ShippingOrder.all().limit(limit).offset((block - 1) * limit)
        result = await VShippingOrder.all().limit(limit).offset((block - 1) * limit)
        # result = await ShippingOrder.all()
        logging.info(result)
        await close_connection()


    except Exception as e:
        logging.error(e)
        await close_connection()
        raise HTTPException(status_code=500, detail="Error in query Shipment Orders")

    return {"message": "Consult Success", "data": result}


# @router.get("/clients")
# async def get_clients():
#     try:
#         await init_db()
#     except Exception as e:
#         logging.error(e)
#         await close_connection()
#         raise HTTPException(status_code=500, detail="Error in init db")
#
#     try:
#
#         clients = await Client.all().values(
#             "id", "name", "type", "project"
#         )
#         await close_connection()
#         return {"message": "Consult Success", "data": clients}
#
#     except Exception as e:
#         logging.error(e)
#         await close_connection()
#         raise HTTPException(status_code=500, detail="Error in query clients")


# @router.get("/shipmentOrders")
# async def get_orders():
#     try:
#         await init_db()
#     except Exception as e:
#         logging.error(e)
#         await close_connection()
#         raise HTTPException(status_code=500, detail="Error in init db")
#
#     try:
#
#         listShipOrders: list[ShipmentOrder] = []
#         listShipOrders.append(order)
#         listShipOrders.append(order)
#
#         await close_connection()
#         return {"message": "Consult Success", "data": listShipOrders}
#
#     except Exception as e:
#         logging.error(e)
#         await close_connection()
#         raise HTTPException(status_code=500, detail="Error in query Shipment Orders")

#Paginador asociado a la orden de embarque.
@router.get("/paginator")
async def get_data(skip: int = 0, limit: int = 10):
    try:
        await init_db()
    except Exception as e:
        logging.error(e)
        await close_connection()
        raise HTTPException(status_code=500, detail="Error in init db")

    try:

        await close_connection()
        return {"message": "Consult Success"}

    except Exception as e:
        logging.error(e)
        await close_connection()
        raise HTTPException(status_code=500, detail="Error in query Paginator")


# Here starts validator OE develpment
@router.get("/validatorOE")
async def get_validator(dateAvailable="2023-12-12"):
    try:
        await init_db()
    except Exception as e:
        logging.error(e)
        await close_connection()
        raise HTTPException(status_code=500, detail="Error in init db")

    try:
        listOrdenEmbarque = []
        ordenCompra = random.choice(listOrdenCompra)
        dateFormat = datetime.date.fromisoformat(dateAvailable)

        if (ordenCompra.FechaAutorizacion > dateFormat):
            logging.info("No se pueden generar ordenes de embarque debido a la fecha de vigencia")

        # Here validated de conditions
        if (ordenCompra.NumEmbarques > 0 and ordenCompra.FechaAutorizacion <= dateFormat):
            iterable = ordenCompra.NumEmbarques
            for i in range(iterable):
                ordenesEmbarque = order
                ordenesEmbarque.shipmentOrderNumber = random.randint(100000, 999999)
                ordenesEmbarque.creationDate = datetime.date.today()
                listOrdenEmbarque.append(ordenesEmbarque)
        # Here can be N orderEmbarques
        if (ordenCompra.NumEmbarques == 0 and ordenCompra.FechaAutorizacion <= dateFormat):
            iterable = ordenCompra.NumEmbarques
            for i in range(iterable):
                ordenesEmbarque = order
                ordenesEmbarque.shipmentOrderNumber = random.randint(100000, 999999)
                ordenesEmbarque.creationDate = datetime.date.today()
                listOrdenEmbarque.append(ordenesEmbarque)

        return {"message": "Consult Success", "data": listOrdenEmbarque}
        # await close_connection()

    except Exception as e:
        logging.error(e)
        await close_connection()
        raise HTTPException(status_code=500, detail="Error in query Validator")


# Creation of service Consulting OE.
# @router.get("/CreacionOE")
# async def creacion_OE(numeroOC="OC-25"):
#     try:
#         await init_db()
#     except Exception as e:
#         logging.error(e)
#         await close_connection()
#         raise HTTPException(status_code=500, detail="Error in init db")
#
#     try:
#         #Here we just bring data to show throught window
#         ordenOC: dict = await (PurchaseOrder.get(orderNumber=numeroOC)
#                                        .values("id", "orderNumber", "numberOfShipments","previousDocNumber")
#         )
#
#         quotation_data = await (Quotation.filter(quotationNumber=ordenOC["previousDocNumber"])
#                 .prefetch_related('client')
#                 .values('origin', 'destination', 'client__name')
#         )
#
#         origin = quotation_data[0]['origin']
#         destination = quotation_data[0]['destination']
#         client_name = quotation_data[0]['client__name']
#
#         Info={
#             'orderNumber':ordenOC["orderNumber"],
#             'client':client_name,
#             'origin':origin,
#             'destination':destination
#         }
#
#
#     except Exception as e:
#         logging.error(e)
#         await close_connection()
#         raise HTTPException(status_code=500, detail=f"Error in query Ordenes de Compra: {e}")
#
#     return {"message": "Consult Success", "data": Info}