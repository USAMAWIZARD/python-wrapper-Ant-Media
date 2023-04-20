import numpy as np
from PIL import Image ,ImageOps
import base64
from io import BytesIO

def onVideoFrame(streamId, width , height ,videoFrame):

    print(streamId, width , height)
    img = Image.frombytes(mode='RGBA', size=(width, height), data=videoFrame)
    img = ImageOps.grayscale(img)


    buffer = BytesIO()
    img.save(buffer, format="PNG")
    image_bytes = buffer.getvalue()

    # Convert the byte array to a Base64-encoded string
    image_base64 = base64.b64encode(image_bytes).decode("utf-8")

    return image_base64