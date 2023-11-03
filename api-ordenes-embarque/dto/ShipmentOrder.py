#Class related to Paginator H.U 2.2
class ShipmentOrder:
    def __init__(self, shipmentOrderNumber, shipment, client, hometown, destinationCity, status, creationDate):
        self.shipmentOrderNumber = shipmentOrderNumber  # Tipo number
        self.shipment = shipment  # Tipo string
        self.client = client  # Tipo string
        self.hometown = hometown  # Tipo string
        self.destinationCity = destinationCity  # Tipo string
        self.status = status  # Tipo string
        self.creationDate = creationDate  # Tipo date

order = ShipmentOrder(
    shipmentOrderNumber=987654,
    shipment="Caja seca 53'",
    client="La Nogalera",
    hometown="Chihuahua, Ch. 31136",
    destinationCity="Cd. JUarez, Ch",
    status="Solicitado",
    creationDate="2023-10-11"
)

objectList=[]

for i in range(100):
    objectList.append(order)