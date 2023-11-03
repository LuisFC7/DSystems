import httpx
from loguru import logger as logging


async def getResponseForRequestConsult(*urls):
    logging.debug(urls)
    dictResponses: dict = {}
    for url in urls:
        dictName = url
        logging.debug(f"dictName: {dictName}")
        dictName = dictName.split('?')
        logging.debug(f"dictName: {dictName}")
        dictName = dictName[0]
        logging.debug(f"dictName: {dictName}")
        posicion = dictName.rfind('/')
        logging.debug(f"posicicion: {posicion}")

        # Extrae el substring después de la última barra "/"
        if posicion != -1:
            logging.debug(f"posicicion: {posicion}")
            dictName = dictName[posicion + 1:]
            logging.debug(f"dictName: {dictName}")

        else:
            # Manejar el caso en el que no se encuentra ninguna barra
            dictName = dictName
            logging.debug(f"dictName: {dictName}")
        async with httpx.AsyncClient() as client:
            request = url
            response = await client.get(request)
            logging.debug(f"response: {response.__dict__.get('_content')}")
            response.raise_for_status()  # Verificar si la solicitud fue exitosa
            logging.info(f"responseJson: {response.json()}")
            # response = response.__dict__.get('_content')
            # response = response.decode('utf-8')
            # response = json.loads(response)
            logging.debug(f"dictName: {dictName}")
            dictResponses[dictName] = response.json()
    return dictResponses


