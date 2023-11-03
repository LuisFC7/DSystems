from tortoise import fields, models


class MooringEquipmentCatalog(models.Model):
    id = fields.IntField(pk=True, source_field="Id")
    name = fields.CharField(max_length=60, default="", source_field="Descripcion")
    status = fields.BooleanField(default=True, source_field="Estatus")
    class Meta:
        table = "CatalogoEquiposAmarreEmbarque"



class MooringEquipment(models.Model):
    id = fields.IntField(pk=True, source_field="Id")
    mooringEquipment = fields.ForeignKeyRelation("models.MooringEquipmentCatalog",
                                                 null=True, source_field="EquipoAmarreId")
    shippingOrder = fields.ForeignKeyField("models.ShippingOrder", null=True, source_field="OrdenEmbarqueId")
    quantity = fields.IntField(default=0, source_field="Cantidad")

    class Meta:
        table = "EquiposAmarreEmbarque"