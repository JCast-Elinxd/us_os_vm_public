package ur_os.virtualmemory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import ur_os.memory.paging.PageTable;
import ur_os.memory.paging.PageTableEntry;

/**
 *
 * @author user
 */
public class PVMM_SecondChance extends ProcessVirtualMemoryManager {

    private LinkedList<Integer> queue;
    private HashMap<Integer, Boolean> referenceBits;
    private int lastProcessedAccess;

    public PVMM_SecondChance() {
        type = ProcessVirtualMemoryManagerType.SECOND_CHANCE;
        queue = new LinkedList<>();
        referenceBits = new HashMap<>();
        lastProcessedAccess = 0;
    }

    @Override
    public int getVictim(LinkedList<Integer> memoryAccesses, PageTable pt) {

        syncQueueWithPageTable(pt);
        updateReferenceBits(memoryAccesses, pt);

        while (!queue.isEmpty()) {

            int candidatePage = queue.removeFirst();
            boolean referenced = referenceBits.getOrDefault(candidatePage, false);

            if (!referenced) {
                referenceBits.remove(candidatePage);
                return candidatePage;
            }

            referenceBits.put(candidatePage, false);
            queue.addLast(candidatePage);
        }

        return -1;
    }

    private void syncQueueWithPageTable(PageTable pt) {

        queue.removeIf(page -> page >= pt.getList().size() || !pt.isPageValid(page));

        ArrayList<Integer> validPages = new ArrayList<>();

        int pageNumber = 0;

        for (PageTableEntry pte : pt.getList()) {
            if (pte.isValid() && !queue.contains(pageNumber)) {
                validPages.add(pageNumber);
            }

            pageNumber++;
        }

        validPages.sort(Comparator.comparingInt(page -> pt.getList().get(page).getClock()));

        for (Integer page : validPages) {
            queue.addLast(page);
            referenceBits.putIfAbsent(page, false);
        }
    }

    private void updateReferenceBits(LinkedList<Integer> memoryAccesses, PageTable pt) {

        for (int i = lastProcessedAccess; i < memoryAccesses.size(); i++) {
            int accessedPage = memoryAccesses.get(i);

            if (accessedPage >= 0 && accessedPage < pt.getList().size()) {
                if (pt.isPageValid(accessedPage)) {
                    referenceBits.put(accessedPage, true);
                }
            }
        }

        lastProcessedAccess = memoryAccesses.size();
    }
}