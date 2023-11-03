from tortoise import fields, models


# El nombre de la clase debe ir en ingles y describir solo contexto de la clase
# El nombre de la clase debe ir en singular
# El nombre de la clase debe ir en PascalCase
# Todos los atributos deben estar mapeados a la base de datos

class UnitTransportType(models.Model):
    # Nomenglatura PascalCase = OrdenCompra | CamelCase = ordenCompra | snake_case = orden_compra
    id = fields.IntField(pk=True, source_field="Id")
    legacyId = fields.IntField(null=True, source_field="LegacyId")
    name = fields.CharField(max_length=60, default="", source_field="NombreUnidad")
    classification = fields.CharField(max_length=12, default="", source_field="Clasificacion")
    openClose = fields.CharField(max_length=12, default="", source_field="AbiertoCerrado")
    status = fields.BooleanField(default=True, source_field="Estatus")

    class Meta:
        table = "TiposUnidadTransporteEmbarques"


class Alliance(models.Model):
    id = fields.IntField(pk=True, source_field="Id")
    legacyId = fields.IntField(null=True, source_field="LegacyId")
    name = fields.CharField(max_length=60, default="", source_field="NombreAlianza")
    subcontractor = fields.BooleanField(default=True, source_field="Subcontratista")
    foreignSubcontractor = fields.BooleanField(default=False, source_field="SubcontratistaExtranjero")
    status = fields.BooleanField(default=True, source_field="Estatus")

    class meta:
        table = "AlianzasEmbarques"


class Trailers(models.Model):
    id = fields.IntField(pk=True, source_field="Id")
    legacyId = fields.IntField(null=True, source_field="LegacyId")
    shipmentOrderNumber = fields.CharField(max_length=20, default="", null=False, source_field="NumeroOrdenEmbarque")
    ShippingOrder = fields.ForeignKeyRelation("models.ShippingOrder", null=True, source_field="OrdenEmbarqueId")
    UnitTransportType = fields.ForeignKeyRelation("models.UnitTransportShipmentType",
                                                          null=True, source_field="TipoUnidadTransporteId")
    alliance = fields.ForeignKeyRelation("models.Alliance", null=True, source_field="AlianzaId")
    economicNumber = fields.CharField(max_length=20, default="", null=False, source_field="NumeroEconomico")

    class Meta:
        table = "RemolquesEmbarques"






