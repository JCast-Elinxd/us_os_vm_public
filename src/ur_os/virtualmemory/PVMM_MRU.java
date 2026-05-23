package ur_os.virtualmemory;

import java.util.LinkedList;
import ur_os.memory.paging.PageTable;
import ur_os.memory.paging.PageTableEntry;

/**
 *
 * @author user
 */
public class PVMM_MRU extends ProcessVirtualMemoryManager {

    public PVMM_MRU() {
        type = ProcessVirtualMemoryManagerType.MRU;
    }

    @Override
    public int getVictim(LinkedList<Integer> memoryAccesses, PageTable pt) {

        int victimPage = -1;
        int mostRecentAccess = -1;
        int newestClock = -1;

        int pageNumber = 0;

        for (PageTableEntry pte : pt.getList()) {

            if (pte.isValid()) {

                int lastAccess = -1;

                for (int i = memoryAccesses.size() - 1; i >= 0; i--) {
                    if (memoryAccesses.get(i) == pageNumber) {
                        lastAccess = i;
                        break;
                    }
                }

                if (lastAccess > mostRecentAccess) {
                    mostRecentAccess = lastAccess;
                    newestClock = pte.getClock();
                    victimPage = pageNumber;
                } else if (lastAccess == mostRecentAccess && pte.getClock() > newestClock) {
                    newestClock = pte.getClock();
                    victimPage = pageNumber;
                }
            }

            pageNumber++;
        }

        return victimPage;
    }
}