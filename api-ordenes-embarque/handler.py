from datetime import datetime, date

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi import APIRouter
from mangum import Mangum
from loguru import logger as logging
from db.connection import init_db, close_connection
from models.OrdenEmbarque import ShippingOrder
from routes.consult import router as consult_router
from utils.get_response_for_request import getResponseForRequestConsult

global mocked_getResponseForRequestConsult

app = FastAPI(
    title="Api Soto Shipping orders",
    description="Esta es la API para ordenes de embarque de Soto",
    version="0.1.0",
    root_path="/dev",  # name of your stage
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(consult_router, prefix="/consult", tags=["consult"])

#MOCKS FOR TESTING
async def mocked_matchingOrder()->int:
    number:int=2
    return number

#Success CASE OE
async def mocked_shippingOrder(orderNumber):
    return {"message": "create shipping order successfully",
            "shippingOrderId": "OE-25",
            "shippingOrderNumber": orderNumber}
async def mocked_error430():
    return {"message": "Shipping Order cannot be created due to date has expired "}


@app.get("/")
async def root():
    return {"message": "Shipping orders Soto"}


@app.post("/create/shipping-order")
async def create_shipping_order(orderNumber: str):
    matchingShippingOrder: int = 0

    # Here we extract info about shippingOrder, is all the information
    #UNCOMMENT ORIGINAL INFO
    # responseConsult: dict = await getResponseForRequestConsult(
    #     f"https://3p4d3t6dqi.execute-api.us-east-1.amazonaws.com/dev/consult/purchase_order/validity?orderNumber={orderNumber}"
    # )

    #TESTING
    responseConsult:dict = mocked_getResponseForRequestConsult

    # Here we extract the essential information
    data = responseConsult.get('validity', {}).get('data', {})

    if not orderNumber:
        raise HTTPException(status_code=400, detail="Order number is required")

    if len(orderNumber) > 20:
        raise HTTPException(status_code=400, detail="Order number is too long")

    try:
        await init_db()
    except Exception as e:
        logging.error(e)
        await close_connection()
        raise HTTPException(status_code=500, detail="Error connecting to database")

    # # Here comes some change
    # try:
    #     # Here we consult if there are more shippingOrders associated with OE
    #     matchingShippingOrder = await (ShippingOrder.filter(previousDocNumber=orderNumber).count())
    #
    # except Exception as e:
    #     logging.error(e)
    #     raise HTTPException(status_code=500, detail="Error creating shipping order")
    # finally:
    #     await close_connection()

    # this variable contains today date, to generate Shipment Order
    dateValidation = date.today()


    #REFACT
    #1. VALIDATION FOR DATE
    if (datetime.strptime(data["validityStartDate"], "%Y-%m-%d").date() <= dateValidation <= datetime.strptime(
            data["validityEndDate"], "%Y-%m-%d").date()):

        #IF ITS VALID VERIFY DB
        try:
            # Here we consult if there are more shippingOrders associated with OE
            #UNCOMMENT ORIGINAL INFO
            # matchingShippingOrder = await (ShippingOrder.filter(previousDocNumber=orderNumber).count())
            #TESTING
            matchingShippingOrder = await mocked_matchingOrder()

        except Exception as e:
            logging.error(e)
            raise HTTPException(status_code=500, detail="Data cannot be read")
        finally:
            await close_connection()

        #VALIDATE NUMBER OF SHIPMENTS (FIRST EQUAL TO ZERO )
        if data["numberOfShipments"] > 0:
            #VALIDATED HOW MANY ORDERS ARE ASSOCIATED
            if (matchingShippingOrder < data["numberOfShipments"]):
                # shippingOrderResult: ShippingOrder = await ShippingOrder.create(previousDocNumber=orderNumber)
                # shippingOrderResult.shippingOrderNumber = f"OE-{shippingOrderResult.id}"
                # await shippingOrderResult.save()
                #
                # return {"message": "create shipping order successfully",
                #         "shippingOrderId": shippingOrderResult.id,
                #         "shippingOrderNumber": shippingOrderResult.shippingOrderNumber}
                return await mocked_shippingOrder(orderNumber)

            else:
                raise HTTPException(status_code=432,
                                    detail="Shipping Order cannot be created due to number has been reached")

        #2. VALIDATION IN CASE EQUAL TO ZERO
        elif data["numberOfShipments"] == 0:
            # shippingOrderResult: ShippingOrder = await ShippingOrder.create(previousDocNumber=orderNumber)
            # shippingOrderResult.shippingOrderNumber = f"OE-{shippingOrderResult.id}"
            # await shippingOrderResult.save()
            #
            # return {"message": "create shipping order successfully",
            #         "shippingOrderId": shippingOrderResult.id,
            #         "shippingOrderNumber": shippingOrderResult.shippingOrderNumber}
            return await mocked_shippingOrder(orderNumber)


        #3. VALIDATION FOR NEGATIVES OR DIFFERENT VALUES OF NUMBER OF SHIPMENTS
        else:
            raise HTTPException(status_code=500,
                                detail="Shipping Order cannot be created because the number shipments is not a valid number")

    else:
        raise HTTPException(status_code=430, detail="Shipping Order cannot be created due to date has expired")


    #OLD WAY
    # Two Ways Validation
    # First Validation, validate date and shipmentsOrders related to DOCNumber
    # if (datetime.strptime(data["validityStartDate"], "%Y-%m-%d").date() <= dateValidation <= datetime.strptime(
    #         data["validityEndDate"], "%Y-%m-%d").date()) and data["numberOfShipments"] > 0:
    #         # Here validated associated OC
    #     if (matchingShippingOrder < data["numberOfShipments"]):
    #         shippingOrderResult: ShippingOrder = await ShippingOrder.create(previousDocNumber=orderNumber)
    #         shippingOrderResult.shippingOrderNumber = f"OE-{shippingOrderResult.id}"
    #         await shippingOrderResult.save()
    #         return {"message": "create shipping order successfully",
    #                 "shippingOrderId": shippingOrderResult.id,
    #                 "shippingOrderNumber": shippingOrderResult.shippingOrderNumber}
    #
    #     else:
    #         raise HTTPException(status_code=432,
    #                                 detail="Shipping Order cannot be created due to number has been reached ")
    #
    # elif data.get("numberOfShipments") < 0:
    #     raise HTTPException(status_code=500, detail="Shipping Order cannot be created because the number shipments is not a valid number ")
    #
    #     # Second validation, validate just for date
    # if (datetime.strptime(data["validityStartDate"], "%Y-%m-%d").date() <= dateValidation <= datetime.strptime(
    #         data["validityEndDate"], "%Y-%m-%d").date()) and data["numberOfShipments"] == 0:
    #     # Make any OE
    #     shippingOrderResult: ShippingOrder = await ShippingOrder.create(previousDocNumber=orderNumber)
    #     shippingOrderResult.shippingOrderNumber = f"OE-{shippingOrderResult.id}"
    #     await shippingOrderResult.save()
    #
    #     return {"message": "create shipping order successfully",
    #             "shippingOrderId": shippingOrderResult.id,
    #             "shippingOrderNumber": shippingOrderResult.shippingOrderNumber}
    #
    #     # Last validation in case that date doesn't have vig
    # if not (datetime.strptime(data["validityStartDate"], "%Y-%m-%d").date() <= dateValidation <= datetime.strptime(
    #         data["validityEndDate"], "%Y-%m-%d").date()):
    #     raise HTTPException(status_code=430, detail="Shipping Order cannot be created due to date has expired ")



        # shippingOrderResult: ShippingOrder = await ShippingOrder.create(previousDocNumber=orderNumber)
        # shippingOrderResult.shippingOrderNumber = f"OE-{shippingOrderResult.id}"
        # await shippingOrderResult.save()


# @app.post("/create/shipping-order")
# async def create_shipping_order(orderNumber: str):
#
#     if not orderNumber:
#         raise HTTPException(status_code=400, detail="Order number is required")
#
#     if len(orderNumber) > 20:
#         raise HTTPException(status_code=400, detail="Order number is too long")
#
#     try:
#         await init_db()
#     except Exception as e:
#         logging.error(e)
#         raise HTTPException(status_code=500, detail="Error connecting to database")
#
#     try:
#         shippingOrderResult: ShippingOrder = await ShippingOrder.create(previousDocNumber=orderNumber)
#         shippingOrderResult.shippingOrderNumber = f"OE-{shippingOrderResult.id}"
#         await shippingOrderResult.save()
#     except Exception as e:
#         logging.error(e)
#         raise HTTPException(status_code=500, detail="Error creating shipping order")
#     finally:
#         await close_connection()
#
#     return {"message": "create shipping order successfully",
#             "shippingOrderId": shippingOrderResult.id,
#             "shippingOrderNumber": shippingOrderResult.shippingOrderNumber}


@app.delete("/delete/shipping-order")
async def delete_shipping_order(shipping_order_id: int):
    try:
        await init_db()
    except Exception as e:
        logging.error(e)
        raise HTTPException(status_code=500, detail="Error connecting to database")

    try:
        result = await ShippingOrder.filter(id=shipping_order_id).delete()
        if result == 0:
            raise HTTPException(status_code=404, detail="Shipping order not found")
    except Exception as e:
        logging.error(e)
        raise HTTPException(status_code=500, detail="Error deleting shipping order")
    finally:
        await close_connection()

    return {"message": "Shipping order deleted successfully"}


handler: Mangum = Mangum(app)