#Class related to Paginator H.U 2.2
class Cotizacion:
    def __init__(self, NumeroCotizacion, ClienteID, Origen, Destino):
        self.NumeroCotizacion = NumeroCotizacion
        self.ClienteID = ClienteID
        self.Origen = Origen
        self.Destino = Destino

cotizacion = Cotizacion(
    NumeroCotizacion=123456,
    ClienteID=678901,
    Origen="CDMX",
    Destino="Cd. Juarez"
)