import pytest
from fastapi.testclient import TestClient

import handler
from handler import app # Importa el manejador de ruta desde handler.py
from unittest.mock import patch


client = TestClient(app=app)

#Mock For OC Data
async def mocked_getResponseForRequestConsult()-> dict:
    simulated_data = {
        'validity' : {
            'data' : {
                'validityType' : 'Numero de Embarques',
                'validityStartDate' : '2023-04-23',
                'validityEndDate' : '2023-12-24',
                'numberOfShipments' : 3
            }
        }
    }
    return simulated_data

# TEST FOR CREATION OF SHIPPING ORDER
@pytest.mark.asyncio
async def test_create_shipping_order():

    simulated_data= await mocked_getResponseForRequestConsult()

    handler.mocked_getResponseForRequestConsult=simulated_data

    with patch('handler.create_shipping_order'):
        response = client.post(url="/create/shipping-order?orderNumber=OC-63")

    assert response.status_code == 200
    assert response.json() == {"message": "create shipping order successfully",
                               "shippingOrderId": "OE-25",
                               "shippingOrderNumber":"OC-63"}

# TEST FOR ERROR 432 FOR SHIPMENT AMOUNT ERROR
@pytest.mark.asyncio
async def test_dateError_shippingOrder():

    simulated_data = await mocked_getResponseForRequestConsult()
    simulated_data['validity']['data']['numberOfShipments']=1

    handler.mocked_getResponseForRequestConsult = simulated_data
    with patch('handler.create_shipping_order'):
        response = client.post(url="/create/shipping-order?orderNumber=OC-63")

    assert response.status_code == 432
    assert response.json() == {'detail': 'Shipping Order cannot be created due to number has been reached'}

# TEST FOR ERROR 430 FOR DATE ERROR
@pytest.mark.asyncio
async def test_dateError2_shippingOrder():

    simulated_data = await mocked_getResponseForRequestConsult()
    simulated_data['validity']['data']['validityEndDate'] = '2023-05-23'

    handler.mocked_getResponseForRequestConsult = simulated_data
    with patch('handler.create_shipping_order'):
        response = client.post(url="/create/shipping-order?orderNumber=OC-63")

    assert response.status_code == 430
    assert response.json() == {'detail': 'Shipping Order cannot be created due to date has expired'}

#TEST FOR NEGATIVE NUMBERS OF SHIPMENTS
@pytest.mark.asyncio
async def test_dateError3_shippingOrder():

    simulated_data = await mocked_getResponseForRequestConsult()
    simulated_data['validity']['data']['numberOfShipments'] = -1

    handler.mocked_getResponseForRequestConsult = simulated_data
    with patch('handler.create_shipping_order'):
        response = client.post(url="/create/shipping-order?orderNumber=OC-63")

    assert response.status_code == 500
    assert response.json() == {'detail': 'Shipping Order cannot be created because the number shipments is not a valid number'}

#TEST WHEN numberOfShipments EQUAL TO ZERO
@pytest.mark.asyncio
async def test_caseCreattion2_shippingOrder():

    simulated_data = await mocked_getResponseForRequestConsult()
    simulated_data['validity']['data']['numberOfShipments'] = 0

    handler.mocked_getResponseForRequestConsult = simulated_data
    with patch('handler.create_shipping_order'):
        response = client.post(url="/create/shipping-order?orderNumber=OC-63")

    assert response.status_code == 200
    assert response.json() == {"message": "create shipping order successfully",
                               "shippingOrderId": "OE-25",
                               "shippingOrderNumber": "OC-63"}

