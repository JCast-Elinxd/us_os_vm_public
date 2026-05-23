import java.util.LinkedList;

import ur_os.memory.paging.PageTable;
import ur_os.virtualmemory.PVMM_FIFO;

public class PVMM_FIFO_Test {

    private static final int[] REFERENCES = {
        7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1
    };

    public static void main(String[] args) {

        PVMM_FIFO fifo = new PVMM_FIFO();

        PageTable pageTable = new PageTable(512, 3, true);

        for (int i = 0; i < 8; i++) {
            pageTable.addFrameID(-1, false);
        }

        LinkedList<Integer> memoryAccesses = new LinkedList<>();

        int pageFaults = 0;
        int loadedPages = 0;
        int nextFrame = 0;

        for (int time = 0; time < REFERENCES.length; time++) {

            int page = REFERENCES[time];

            if (pageTable.isPageValid(page)) {
                memoryAccesses.add(page);

                System.out.println(
                    "Paso " + (time + 1) +
                    " | Pagina " + page +
                    " | HIT"
                );

            } else {
                pageFaults++;

                int frame;

                if (loadedPages < 3) {
                    frame = nextFrame;
                    nextFrame++;
                    loadedPages++;

                    System.out.println(
                        "Paso " + (time + 1) +
                        " | Pagina " + page +
                        " | FAULT | Marco libre " + frame
                    );

                } else {
                    int victim = fifo.getVictim(memoryAccesses, pageTable);
                    frame = pageTable.getFrameIdFromPage(victim);
                    pageTable.setPageValid(victim, false);

                    System.out.println(
                        "Paso " + (time + 1) +
                        " | Pagina " + page +
                        " | FAULT | Victima " + victim
                    );
                }

                pageTable.setFrameID(page, frame);
                pageTable.getList().get(page).setClock(time);

                memoryAccesses.add(page);
            }
        }

        System.out.println("----------------------------------");
        System.out.println("FIFO page faults = " + pageFaults);
        System.out.println("Resultado esperado = 15");
    }
}