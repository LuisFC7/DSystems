from datetime import datetime
from uuid import uuid4

from tortoise import fields, models


def get_default_datetime():
    return datetime.strptime("1900-01-01 00:00:00", '%Y-%m-%d %H:%M:%S')


def get_default_date():
    return datetime.strptime("1900-01-01", '%Y-%m-%d')


class ShippingRequest(models.Model):
    id = fields.IntField(pk=True, source_field="Id")
    actualState = fields.CharField(max_length=20, default="", source_field="EstadoActual")
    confirmationDate = fields.DatetimeField(default=get_default_datetime(), source_field="FechaConfirmacion")
    status = fields.BooleanField(default=True, source_field="Estatus")
    createdAt = fields.DatetimeField(auto_now_add=True, source_field="CreadoEl")
    updatedAt = fields.DatetimeField(auto_now=True, source_field="ModificadoEl")

    class Meta:
        table = "SolicitudesEmbarque"


class Operators(models.Model):
    id = fields.IntField(pk=True, source_field="Id")
    legacyId = fields.IntField(null=True, source_field="LegacyId")
    name = fields.CharField(max_length=50, default="", source_field="NombreOperador")
    lastName = fields.CharField(max_length=50, default="", source_field="ApellidoPaterno")
    secondLastName = fields.CharField(max_length=50, default="", source_field="ApellidoMaterno")
    rfc = fields.CharField(max_length=18, default="", source_field="Rfc")
    status = fields.BooleanField(default=True, source_field="Estatus")
    createdAt = fields.DatetimeField(auto_now_add=True, source_field="CreadoEl")
    updatedAt = fields.DatetimeField(auto_now=True, source_field="ModificadoEl")

    class Meta:
        table = "Operadores"


class ShippingOrder(models.Model):
    id = fields.IntField(pk=True, source_field="Id")
    uuid = fields.UUIDField(default=uuid4, source_field="UUID", unique=True)
    purchaseOrderGuid = fields.UUIDField(source_field="OrdenCompraGuid")
    statusOE = fields.CharField(max_length=20, default="", source_field="EstatusOE")
    shippingOrderNumber = fields.CharField(max_length=20, default="OE-", source_field="NumeroOrdenEmbarque",
                                           unique=True)
    shippingRequest = fields.ForeignKeyRelation("models.ShippingRequest", null=True, source_field="SolicitudEmbarqueId")
    shippingOrderDate = fields.DateField(default=get_default_date(), source_field="FechaOrdenEmbarque")
    previousDocType = fields.CharField(max_length=30, default="", source_field="TipoDocAnterior")
    previousDocNumber = fields.CharField(max_length=50, default="", source_field="NumeroDocAnterior", null=True)
    expectedCompletionDate = fields.DateField(default=get_default_date(),
                                              source_field="FechaTerminoEstimada")
    realKms = fields.FloatField(default=0, source_field="KmsReales")
    emptyKms = fields.FloatField(default=0, source_field="KmsVacios")
    expectedKms = fields.FloatField(default=0, source_field="KmsEstimados")
    invoiceComments = fields.CharField(max_length=250, null=True, default="", source_field="ComentariosFactura")
    sender = fields.CharField(max_length=30, default="", source_field="Remitente")
    receiver = fields.CharField(max_length=30, default="", source_field="Destinatario")
    operator = fields.ForeignKeyRelation("models.Operators", null=True, source_field="OperadorId")
    tractor = fields.CharField(max_length=30, default="", null=True, source_field="TractorId")
    licensePlates = fields.CharField(max_length=20, default="", null=True, source_field="Placas")
    mileage = fields.FloatField(default=0, null=True, source_field="Kilometraje")
    trailerNumber = fields.CharField(max_length=20, default="", null=True, source_field="NumRemolque")
    cross = fields.BooleanField(default=False, null=True, source_field="Cruce")
    movStartDate = fields.DatetimeField(default=get_default_datetime(), source_field="FechaInicioMov")
    movEndDate = fields.DatetimeField(default=get_default_datetime(), source_field="FechaFinMov")
    trailer = fields.CharField(max_length=50, default="", null=True, source_field="Remolque")
    typeMovement = fields.CharField(max_length=50, default="", source_field="TipoMovimiento")
    emptyLoaded = fields.BooleanField(default=False, null=True, source_field="CargadoVacio")
    movementInstructions = fields.CharField(max_length=250, default="", null=True, source_field="InstruccionesMovimiento")
    status = fields.BooleanField(default=True, source_field="Estatus")
    createdAt = fields.DatetimeField(auto_now_add=True, source_field="CreadoEl")
    updatedAt = fields.DatetimeField(auto_now=True, source_field="ModificadoEl")

    class Meta:
        table = "OrdenesEmbarque"
