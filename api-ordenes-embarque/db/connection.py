from tortoise import Tortoise
import os
from dotenv import load_dotenv
from loguru import logger as logging

load_dotenv(os.getcwd() + "/env/.env")

logging.debug(os.getcwd())

logging.debug(os.getenv('DB_USER'))
logging.debug(os.getenv('DB_PASS'))
logging.debug(os.getenv('DB_HOST'))
logging.debug(os.getenv('DB_NAME'))
logging.debug(os.getenv('DB_PORT'))
logging.debug(os.getenv('DB_DRIVER'))


dbSoto: dict[str, str | None] = {
    'user': os.getenv('DB_USER'),
    'pwd': os.getenv('DB_PASS'),
    'host': os.getenv('DB_HOST'),
    'db': os.getenv('DB_NAME'),
    'port': str(os.getenv('DB_PORT')),
    'driver': os.getenv('DB_DRIVER')
}

print(dbSoto)

async def init_db():
    logging.debug(f'data env: {dbSoto}')
    logging.debug(f'Connecting to {dbSoto.get("db")} database')
    await Tortoise.init(
        db_url=f'mssql://{dbSoto.get("user")}:{dbSoto.get("pwd")}@{dbSoto.get("host")}:{dbSoto.get("port")}/'
               f'{dbSoto.get("db")}?driver={dbSoto.get("driver")};&TrustServerCertificate=no',
        modules={'models': [
            'models.OrdenEmbarque',
            'models.views.VOrdenEmbarque',
            'models.views.VOrdenesCompraAuth',
        ]}
    )
    # await Tortoise.init(
    #     db_url='mssql://usrtsoto:s0t023.!@roatech.dyndns.info:8008/Tsoto_CRM_Ms?driver=/opt/microsoft/msodbcsql17/lib64/libmsodbcsql-17.7.so.2.1;&TrustServerCertificate=yes',
    #     modules={'models': [
    #         'models.Aduanas',
    #         'models.Aseguradoras',
    #         'models.Crm',
    #         'models.DatosFiscales',
    #         'models.Domicilios',
    #         'models.EquiposAmarre',
    #         'models.OrdenCompra',
    #         'models.Remolques',
    #     ]}
    # )
    # await Tortoise.init(
    #     db_url='mssql://usrtsoto:s0t023.!@roatech.dyndns.info:8008/Tsoto_CRM_Ms?driver=ODBC+Driver+18+for+SQL+Server;&TrustServerCertificate=yes',
    #     modules={'models': [
    #         'models.Aduanas',
    #         'models.Aseguradoras',
    #         'models.Crm',
    #         'models.DatosFiscales',
    #         'models.Domicilios',
    #         'models.EquiposAmarre',
    #         'models.OrdenCompra',
    #         'models.Remolques',
    #     ]}
    # )


async def close_connection():
    await Tortoise.close_connections()
