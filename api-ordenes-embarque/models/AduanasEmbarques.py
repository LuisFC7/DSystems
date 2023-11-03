from tortoise import fields, models


class CustomsAgencies(models.Model):
    id = fields.IntField(pk=True, source_field="Id")
    legacyId = fields.IntField(null=True, source_field="LegacyId")
    name = fields.CharField(max_length=60, default="", source_field="NombreAgenciaAduanal")
    type = fields.CharField(max_length=60, default="", source_field="Tipo")
    address = fields.ForeignKeyField("models.Address", null=True, source_field="DireccionId")
    status = fields.BooleanField(default=True, source_field="Estatus")

    class Meta:
        table = "AgenciasAduanalesEmbarques"


class CustomsShipping(models.Model):
    id = fields.IntField(pk=True, source_field="Id")
    shippingOrder = fields.ForeignKeyField("models.ShippingOrder", null=True, source_field="OrdenEmbarqueId")
    customAgency = fields.ForeignKeyField("models.CustomsAgencies", null=True, source_field="AgenciaAduanalId")
    releaseIn = fields.CharField(max_length=50, default="", source_field="LiberarEn")
    port = fields.CharField(max_length=50, default="", source_field="PuertoCruce")

    class Meta:
        table = "AduanasEmbarques"
