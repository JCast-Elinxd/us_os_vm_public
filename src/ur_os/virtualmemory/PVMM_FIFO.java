package ur_os.virtualmemory;

import java.util.LinkedList;
import ur_os.memory.paging.PageTable;
import ur_os.memory.paging.PageTableEntry;

/**
 *
 * @author user
 */
public class PVMM_FIFO extends ProcessVirtualMemoryManager {

    public PVMM_FIFO() {
        type = ProcessVirtualMemoryManagerType.FIFO;
    }

    @Override
    public int getVictim(LinkedList<Integer> memoryAccesses, PageTable pt) {

        int victimPage = -1;
        int oldestClock = Integer.MAX_VALUE;

        int pageNumber = 0;

        for (PageTableEntry pte : pt.getList()) {

            if (pte.isValid()) {
                if (pte.getClock() < oldestClock) {
                    oldestClock = pte.getClock();
                    victimPage = pageNumber;
                }
            }

            pageNumber++;
        }

        return victimPage;
    }
}
