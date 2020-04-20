package com.aws.wj;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;

public class ProgressListenerImpl implements ProgressListener {
    Long totalTransferred;
    Long toTransfer;

//    public ProgressListenerImpl(){
//        this.totalTransferred=0L;
//    }


    @Override
    public void progressChanged(ProgressEvent evt){
        ProgressEventType type = evt.getEventType();
        switch(type){
            case TRANSFER_PREPARING_EVENT:
                System.out.println("Preparing for transfer...");
                break;

            case TRANSFER_STARTED_EVENT:
                System.out.println("\nBeginning transfer process...");
                break;

            case CLIENT_REQUEST_STARTED_EVENT:
                System.out.println("\nFirst request started");
                totalTransferred=0L;
                break;

            case REQUEST_CONTENT_LENGTH_EVENT:
                toTransfer=evt.getBytes();
                break;

            case HTTP_REQUEST_CONTENT_RESET_EVENT:
                System.out.println("\n\nResetting content...");
               // totalTransferred+=totalTransferred;//or set to 0?
//                totalTransferred=0L;
                break;

            case HTTP_REQUEST_STARTED_EVENT:
                System.out.println("\nNext request started");
                totalTransferred=0L;
               // totalTransferred=evt.getBytesTransferred();
                break;

            case HTTP_REQUEST_COMPLETED_EVENT:
                System.out.println("\nHTTP request completed");
                break;

            case HTTP_RESPONSE_STARTED_EVENT:
                break;

            case HTTP_RESPONSE_COMPLETED_EVENT:
                break;

            case CLIENT_REQUEST_SUCCESS_EVENT:
                System.out.println("\nClient request success");
                break;

            case CLIENT_REQUEST_FAILED_EVENT:
                System.err.println("\nClient request failed");
                break;

            case CLIENT_REQUEST_RETRY_EVENT:
                System.out.println("\nClient request retry");
                totalTransferred=0L;
                break;


            case REQUEST_BYTE_TRANSFER_EVENT:
                totalTransferred+=evt.getBytesTransferred();
                long percentage=getPercentage(totalTransferred,toTransfer);
                System.out.print(String.format("\r Progress: %s/%s bytes transferred - %d%% completed",totalTransferred,toTransfer,percentage));
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
