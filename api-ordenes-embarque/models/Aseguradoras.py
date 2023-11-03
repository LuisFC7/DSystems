from tortoise import fields, models


class InsureCompanies(models.Model):
    id = fields.IntField(pk=True, source_field="Id")
    legacyId = fields.IntField(null=True, source_field="LegacyId")
    name = fields.CharField(max_length=60, default="", source_field="NombreAseguradora")
    status = fields.BooleanField(default=True, source_field="Estatus")

    class Meta:
        table = "AseguradorasEmbarques"


class InsureShippingOrder(models.Model):
    id = fields.IntField(pk=True, source_field="Id")
    insureCompany = fields.ForeignKeyRelation("models.InsureCompanies",
                                              null=True, source_field="AseguradoraId")
    policyNumber = fields.CharField(max_length=20, default="", source_field="NumeroPoliza")
    premiumInsurance = fields.FloatField(default=0, source_field="PrimarSeguro")
    merchandiseValue = fields.FloatField(default=0, source_field="ValorMercancia")

    class Meta:
        table = "AseguradoraOE"