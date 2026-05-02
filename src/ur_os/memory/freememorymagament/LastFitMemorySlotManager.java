package ur_os.memory.freememorymagament;

public class LastFitMemorySlotManager extends FreeMemorySlotManager {

    public LastFitMemorySlotManager(int memSize) {
        super(memSize);
    }

    @Override
    public MemorySlot getSlot(int size) {
        MemorySlot lastSlot = null;

        for (MemorySlot memorySlot : list) {
            if (memorySlot.canContain(size)) {
                lastSlot = memorySlot;
            }
        }

        if (lastSlot == null) {
            System.out.println("Error - Memory cannot allocate a slot big enough for the requested memory");
            return null;
        }

        if (lastSlot.getSize() == size) {
            list.remove(lastSlot);
            return lastSlot;
        } else {
            return lastSlot.assignMemory(size);
        }
    }
}