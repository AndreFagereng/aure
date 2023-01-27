import boto3
import os
import sys
import uuid
from urllib.parse import unquote_plus
from PIL import Image
import PIL.Image

s3_client = boto3.client('s3')

def resize_image(image_path, resized_path):
    with Image.open(image_path) as image:
        image.thumbnail(tuple(x / 4 for x in image.size))
        image.save(resized_path)

def lambda_handler(event, context):
    for record in event['Records']:
        print(record)
        bucket = record['s3']['bucket']['name']
        key = unquote_plus(record['s3']['object']['key'])

        path, filename = os.path.split(key)

        print("Path {}, filename {}".format(path, filename))

        # Path to original image
        # Path to saved resized image
        local_path_to_original_image = os.path.join("/tmp", filename)
        local_path_to_resized_image = os.path.join("/tmp", "thumbnail-" + filename)
        s3_path_to_resized_image = os.path.join(path.replace("original_images/",
                                                             "thumbnails/"), filename)

        print(f"Downloading {key} from {bucket} to local file {local_path_to_original_image}")
        s3_client.download_file(bucket, key, local_path_to_original_image)

        print(f"Resizing and saving to {local_path_to_resized_image}")
        resize_image(local_path_to_original_image, local_path_to_resized_image)

        print(f"Uploading {local_path_to_resized_image} to {bucket} with filepath {s3_path_to_resized_image}")
        s3_client.upload_file(local_path_to_resized_image, bucket, s3_path_to_resized_image)
