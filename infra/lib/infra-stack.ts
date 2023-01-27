import * as cdk from 'aws-cdk-lib';
import * as lambda from 'aws-cdk-lib/aws-lambda';
import * as s3 from 'aws-cdk-lib/aws-s3';
import * as s3n from 'aws-cdk-lib/aws-s3-notifications'
import * as path from 'path';
import { Construct } from 'constructs';
import * as lambdaEventSources from 'aws-cdk-lib/aws-lambda-event-sources';
import * as python from '@aws-cdk/aws-lambda-python-alpha'

export class InfraStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    const thumbnailLambda = new python.PythonFunction(this, 'ThumbnailLambda', {
      entry: 'lib/lambda',
      functionName: 'thumbnailLambda',
      runtime: lambda.Runtime.PYTHON_3_9,
      index: 'thumbnail_lambda.py',
      handler: 'lambda_handler',
    });

    const imageBucket = s3.Bucket.fromBucketArn(this, 'ImageBucket', 'arn:aws:s3:::aure-image-storage');
    imageBucket.addEventNotification(s3.EventType.OBJECT_CREATED_PUT, new s3n.LambdaDestination(thumbnailLambda),
    {prefix: 'original_images/'});

  }
}
