public class Store {
    int currentTime;
    Register[] registers = null;

    public Store(int setCurrentTime, int setNumRegisters) throws IllegalArgumentException {
        currentTime = setCurrentTime;
        if (setNumRegisters <= 0) { throw new IllegalArgumentException("Store can't have <= 0 registers."); }
        registers = new Register[setNumRegisters];
        for (int i = 0; i < registers.length; i++) {
            int minutesPerItem = i != registers.length-1 ? 1 : 2;
            registers[i] = new Register((i+1), minutesPerItem);
        }
    }

    public void printStore() {
        for (int i = 0; i < registers.length; i++) { System.out.println(registers[i].getLine().toString()); }
    }

    public Register getRegister(int registerId) throws IllegalArgumentException {
        if (registerId <= 0 || registerId > registers.length) {
            throw new IllegalArgumentException("Register ID out of bounds. Note that registers IDs start from 1.");
        }
        return registers[registerId-1];
    }

    public Register getRegisterWithShortestLine() {
        Register minRegister = registers[0];
        int minLineSize = minRegister.getLineSize();
        if (minLineSize == 0) { return minRegister; } // don't even look for a shorter line
        for (int i = 1; i < registers.length; i++) {
            Register currRegister = registers[i];
            int currLineSize = currRegister.getLineSize();
            if (currLineSize < minLineSize) { minRegister = currRegister; minLineSize = currLineSize; }
        }
        return minRegister;
    }

    public Register getRegisterWithLastCustomerLeastItems() {
        Register minRegister = registers[0];
        if (minRegister.getLineSize() == 0) { return minRegister; } // OR return least idx empty line
        int minItems = minRegister.getBackCustomer().numItems;
        for (int i = 1; i < registers.length; i++) {
            Register currRegister = registers[i];
            if (currRegister.getLineSize() == 0) { return currRegister; } // OR return least idx empty line
            int currItems = currRegister.getBackCustomer().numItems;
            if (currItems < minItems) { minRegister = currRegister; minItems = currItems; }
        }
        return minRegister;
    }

    public void addCustomer(Customer c) throws IllegalArgumentException {
        if (c.arrivalTime < currentTime) { throw new IllegalArgumentException("Customer order error."); }
        if (c.arrivalTime > currentTime) {
            while (currentTime < c.arrivalTime) { addMinute(); } // advance simulation to this customer's arrival time
        }
        switch (c.type) {
            case 'A':
                getRegisterWithShortestLine().assignCustomer(c);
                break;
            case 'B':
                getRegisterWithLastCustomerLeastItems().assignCustomer(c);
                break;
            default:
                throw new IllegalArgumentException("Customer type other than A or B given");
        }
        startServicingFrontCustomersIfNotAlready(); // if c is the first customer in a register's line
    }

    public void updateFrontCustomersIfFinished() {
        for (Register r : registers) {
            Customer frontCustomer = r.getFrontCustomer();
            if (frontCustomer != null && frontCustomer.serviceTime + r.getMinutesPerItem()*frontCustomer.numItems == currentTime) {
                r.removeFrontCustomer();
                Customer newFrontCustomer = r.getFrontCustomer();
                if (newFrontCustomer != null) {
                    assert(newFrontCustomer.serviceTime == null);
                    newFrontCustomer.setServiceTime(currentTime);
                }
            }
        }
    }

    public void startServicingFrontCustomersIfNotAlready() {
        for (Register r : registers) {
            Customer frontCustomer = r.getFrontCustomer();
            if (frontCustomer != null && frontCustomer.serviceTime == null) { frontCustomer.serviceTime = currentTime; }
        }
    }

    public void addMinute() {
        currentTime += 1;
        updateFrontCustomersIfFinished();
    }

    public boolean areAllRegistersEmpty() {
        for (Register r : registers) { if (r.getLineSize() > 0) { return false; } }
        return true;
    }

    public int getCurrentTime() { return currentTime; }
}
