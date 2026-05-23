package ur_os.virtualmemory;

import java.util.LinkedList;
import ur_os.memory.paging.PageTable;
import ur_os.memory.paging.PageTableEntry;

/**
 *
 * @author user
 */
public class PVMM_LFU extends ProcessVirtualMemoryManager {

    public PVMM_LFU() {
        type = ProcessVirtualMemoryManagerType.LFU;
    }

    @Override
    public int getVictim(LinkedList<Integer> memoryAccesses, PageTable pt) {

        int victimPage = -1;
        int lowestFrequency = Integer.MAX_VALUE;
        int oldestClock = Integer.MAX_VALUE;

        int pageNumber = 0;

        for (PageTableEntry pte : pt.getList()) {

            if (pte.isValid()) {

                int frequency = 0;

                for (Integer accessedPage : memoryAccesses) {
                    if (accessedPage == pageNumber) {
                        frequency++;
                    }
                }

                if (frequency < lowestFrequency) {
                    lowestFrequency = frequency;
                    oldestClock = pte.getClock();
                    victimPage = pageNumber;
                } else if (frequency == lowestFrequency && pte.getClock() < oldestClock) {
                    oldestClock = pte.getClock();
                    victimPage = pageNumber;
                }
            }

            pageNumber++;
        }

        return victimPage;
    }
}