from tortoise import models, fields


class VPurchaseOrdersAuth(models.Model):
    id = fields.IntField(pk=True, source_field="IdOC")
    orderNumber = fields.CharField(max_length=20, source_field="NumeroOrdenCompra")
    guid = fields.CharField(max_length=16, source_field="GuidOc")
    endValidityDate = fields.DateField(source_field="Vigencia")
    originCity = fields.CharField(max_length=72, source_field="CiudadOrigen")
    destinationCity = fields.CharField(max_length=72, source_field="CiudadDestino")
    origin = fields.CharField(max_length=124, source_field="Origen")
    destination = fields.CharField(max_length=124, source_field="Destino")
    client = fields.CharField(max_length=50, source_field="Cliente")
    serviceType = fields.CharField(max_length=60, source_field="TipoServicio")
    hazmat = fields.BooleanField(source_field="Hazmat")
    alliance = fields.CharField(max_length=5, source_field="Alianza")
    authDate = fields.DateField(source_field="Autorizacion")

    class Meta:
        table = "VListadoOCAutorizadas"




