package com.aws.wj;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;

public class ProgressListenerImpl implements ProgressListener {
    Long totalTransferred;
    Long toTransfer;
    Long multipartNum;
    Long fileSize;
    Long overallTransferred;


    public ProgressListenerImpl(Long fileSize){
        this.fileSize=fileSize;
        this.overallTransferred=0L;
    }


    @Override
    public void progressChanged(ProgressEvent evt){
        ProgressEventType type = evt.getEventType();
        switch(type){
            case TRANSFER_PREPARING_EVENT:
                System.out.println("Preparing for transfer...");
                break;

            case TRANSFER_STARTED_EVENT:
                System.out.println("\nBeginning transfer process...");
                multipartNum=0L;
                break;

            case CLIENT_REQUEST_STARTED_EVENT:
                multipartNum++;
                totalTransferred=0L;
                System.out.print(String.format("\nClient request started for multipart number %s\n",multipartNum));
                break;

            case REQUEST_CONTENT_LENGTH_EVENT:
                toTransfer=evt.getBytes();
                Long estMultipartsLeft=((fileSize-overallTransferred)/toTransfer)+1L;
                System.out.print(String.format("\nEstimated multiparts left %s\n",estMultipartsLeft));
                break;

            case HTTP_REQUEST_CONTENT_RESET_EVENT:
                System.out.println("\n\nResetting content...\n");
                multipartNum--;
                break;

            case HTTP_REQUEST_STARTED_EVENT:
                multipartNum++;
                System.out.print(String.format("\nHttp upload request for multipart number %s\n",multipartNum));
                totalTransferred=0L;
               // totalTransferred=evt.getBytesTransferred();
                break;

            case HTTP_REQUEST_COMPLETED_EVENT:
                System.out.println(String.format("\nHTTP request completed.\n"));
                break;

            case HTTP_RESPONSE_STARTED_EVENT:
                break;

            case HTTP_RESPONSE_COMPLETED_EVENT:
                break;

            case CLIENT_REQUEST_SUCCESS_EVENT:
                overallTransferred+=totalTransferred;
                Long overallProgress=(overallTransferred*100)/fileSize;
                System.out.print(String.format("\nMultipart number %s uploaded successfully. Overall progress - %d%%\n",multipartNum, overallProgress));
                break;

            case CLIENT_REQUEST_FAILED_EVENT:
                System.err.println("\nClient request failed\n");
                break;

            case CLIENT_REQUEST_RETRY_EVENT:
                System.out.println("\nClient request retry\n");
                totalTransferred=0L;
                break;


            case REQUEST_BYTE_TRANSFER_EVENT:
                totalTransferred+=evt.getBytesTransferred();
                long percentage=getPercentage(totalTransferred,toTransfer);
                System.out.print(String.format("\r Progress: %s/%s bytes - %d%% completed",totalTransferred,toTransfer,percentage));
                break;

            default:
                System.out.println("\nEvent: "+evt);
                System.out.println("bytes: " +evt.getBytes() + " bytesTransferred: "+evt.getBytesTransferred());
        }

    }

    public static long getPercentage(Long totalTransferred,Long toTransfer){
        double percentage= ((double)totalTransferred/(double)toTransfer)*100.0;
        return Double.valueOf(percentage).longValue();
    }

}
