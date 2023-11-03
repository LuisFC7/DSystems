from tortoise import models, fields


class VShippingOrder(models.Model):
    shippingOrderNumber = fields.CharField(max_length=20, source_field="NumeroOrdenEmbarque", pk=True)
    trailer = fields.CharField(max_length=50, source_field="NombreUnidad")
    client = fields.CharField(max_length=50, source_field="Cliente")
    origin = fields.CharField(max_length=50, source_field="CiudadOrigen")
    destination = fields.CharField(max_length=50, source_field="CiudadDestino")
    dateCreate = fields.DateField(source_field="FechaCreacion")
    status = fields.BooleanField(source_field="Estatus")

    class Meta:
        table = "VListaOE"
