package сhat.client.pushhandler;

import messages.response.Response;

import java.io.IOException;
import java.io.ObjectInputStream;

public class PushHandler implements Runnable {
    private static final String PUSH_HEADER = "PUSH";
    private static final String SERVICE_HEADER = "SERVICE";
    private static final int PUSH_TRIM_LENGTH = 15;
    private static final long TIME_TO_SLEEP = 1000; //mills

    public static void HandleResponse(Response resp) {
        switch(resp.type) {
            case PUSH:
                System.out.println(PUSH_HEADER);
                System.out.println("from: " + resp.message.sender);
                String postfix = "...";
                if (resp.message.text.length() < PUSH_TRIM_LENGTH) {
                    postfix = "";
                }
                System.out.println("content: " + resp.message.text.substring(0, PUSH_TRIM_LENGTH) + postfix);
                break;
            case SERVICE_MESSAGE:
                System.out.println(SERVICE_HEADER);
                System.out.println(resp.service);
        }
    }

    private boolean busy = false;
    private final ObjectInputStream input;

    public void getInput() {
        busy = true;
    }

    public void releaseInput() {
        busy = false;
    }

    public PushHandler(ObjectInputStream is) {
        this.input = is;
    }

    @Override
    public void run() {
        while (true) {
            try {
                while (!busy) {
                    if (input.available() > 0) {
                        var resp = (Response) input.readObject();
                        HandleResponse(resp);
                    }
                }
                while (busy) {
                    Thread.sleep(TIME_TO_SLEEP);
                }
            } catch (Exception e) {
                // fixme
                System.out.println("something terrible is going on");
            }
        }
    }
}
