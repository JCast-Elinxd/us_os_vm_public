/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.freememorymagament;

/**
 *
 * @author super
 */
public class NextFitMemorySlotManager extends FreeMemorySlotManager {
    
    private int lastIndex;
    
    public NextFitMemorySlotManager(int memSize) {
        super(memSize);
        lastIndex = 0;
    }
    
    @Override
    public MemorySlot getSlot(int size) {
        if (list.isEmpty()) {
            System.out.println("Error - No free memory slots available");
            return null;
        }
        
        int n = list.size();
        int startIndex = lastIndex;
        
        for (int checked = 0; checked < n; checked++) {
            int currentIndex = (startIndex + checked) % n;
            MemorySlot memorySlot = list.get(currentIndex);
            
            if (memorySlot.canContain(size)) {
                MemorySlot assignedSlot;
                
                if (memorySlot.getSize() == size) {
                    assignedSlot = memorySlot;
                    list.remove(currentIndex);
                    
                    if (list.isEmpty()) {
                        lastIndex = 0;
                    } else if (currentIndex >= list.size()) {
                        lastIndex = 0;
                    } else {
                        lastIndex = currentIndex;
                    }
                } else {
                    assignedSlot = memorySlot.assignMemory(size);
                    lastIndex = currentIndex;
                }
                
                return assignedSlot;
            }
        }
        
        System.out.println("Error - Memory cannot allocate a slot big enough for the requested memory");
        return null;
    }
}