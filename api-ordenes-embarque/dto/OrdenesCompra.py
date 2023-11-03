#Class related to Paginator H.U 2.2
import datetime
import random
class OrdenesCompra:
    def __init__(self,id,NumOrder,NumEmbarques, FechaAutorizacion, NumDocAnterior):
        self.id=id,
        self.NumOrder = NumOrder,
        self.NumEmbarques = NumEmbarques,
        self.FechaAutorizacion = datetime.date.fromisoformat(FechaAutorizacion),
        self.NumDocAnterior = NumDocAnterior

OrdenCompra = OrdenesCompra(
    id=111111,
    NumOrder="OC-18",
    NumEmbarques=3,
    FechaAutorizacion="2023-12-13",
    NumDocAnterior=123456
)

listOrdenCompra=[]

for i in range(0,10):
    OrdenCompra.NumEmbarques=random.randint(100000, 999999)
    listOrdenCompra.append(OrdenCompra)
