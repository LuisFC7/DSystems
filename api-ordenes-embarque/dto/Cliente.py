#Class related to Paginator H.U 2.2
class Cliente:
    def __init__(self, ClienteID, NombreCliente):
        self.ClienteID = ClienteID
        self.NombreCliente = NombreCliente

cliente = Cliente(
    ClienteID=678901,
    NombreCliente="Miller Freight"
)