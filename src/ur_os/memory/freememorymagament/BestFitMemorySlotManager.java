/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.freememorymagament;

/**
 *
 * @author super
 */
public class BestFitMemorySlotManager extends FreeMemorySlotManager{
    
    public BestFitMemorySlotManager(int memSize){
        super(memSize);
    }
    
   @Override
    public MemorySlot getSlot(int size) {
        MemorySlot bestSlot = null;
        int bestRemainder = Integer.MAX_VALUE;

        for (MemorySlot memorySlot : list) {
            if (memorySlot.canContain(size)) {
                int remainder = memorySlot.getRemainder(size);

                if (remainder < bestRemainder) {
                    bestRemainder = remainder;
                    bestSlot = memorySlot;
                }
            }
        }

        if (bestSlot == null) {
            System.out.println("Error - Memory cannot allocate a slot big enough for the requested memory");
            return null;
        }

        if (bestSlot.getSize() == size) {
            list.remove(bestSlot);
            return bestSlot;
        } else {
            return bestSlot.assignMemory(size);
        }
    }
    
}


