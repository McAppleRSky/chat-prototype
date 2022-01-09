package rarus.chat.server._frontEnd;

import org.junit.jupiter.api.Test;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

// https://youtu.be/fFekJ7myksk
// https://youtu.be/5MjayZBbVdE
// https://alekseygulynin.ru/chat-na-java-servernaya-chast/

class ServerComponentSyncTest implements Runnable{

    static final int TARGET_THREAD_COUNT = 1_000;
    static AtomicInteger remainsOpenedN = new AtomicInteger(TARGET_THREAD_COUNT),
            closedN = new AtomicInteger(0);
    static int openedMain, closedMain;
    volatile CloseableTest closeable;
    boolean waitAcceptClient = true;
    Object sync = new Object();

    ServerComponentSyncTest() throws IOException {

        try(CloseableTest closeable = new CloseableTest() {

            {
                ++ServerComponentSyncTest.openedMain;
            }

            @Override
            public Closeable accept() {

                return new Closeable() {

                    {
                        ServerComponentSyncTest.remainsOpenedN.decrementAndGet();
                    }

                    @Override
                    public void close() {
                        ServerComponentSyncTest.closedN.incrementAndGet();
                    }

                };
            }

            @Override
            public void close() {
                ++ServerComponentSyncTest.closedMain;
            }

        }) {
            this.closeable = closeable;
            synchronized (sync) {
                while (waitAcceptClient){
                    new Thread(this).start();
                    sync.wait();
                    waitAcceptClient = (remainsOpenedN.intValue()>0) ;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (Closeable closable0 = closeable.accept()) {
            synchronized (sync) {
                sync.notify();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void waitTest() {
        assertEquals(1, openedMain);
        assertEquals(1, closedMain);
        assertEquals(0, remainsOpenedN.intValue());
        assertEquals(TARGET_THREAD_COUNT, closedN.intValue());
    }

}

interface CloseableTest extends Closeable {

    Closeable accept();

}
