package asm;

public class Engine {
	boolean running = false;
	int program_counter;
	int accumulator;
	int x_register;
	int stack_pointer;
	int cmp_result;
	int[] memory;
	public Engine (int memoryAmount) {
		//memoryAmount is the number of 4-byte ints allocated for runtime
		running = true;
		accumulator = 0;
		x_register = 0;
		stack_pointer = 0;
		cmp_result = 0;
		memory = new int[memoryAmount];
	}
	public void loadData (byte[] data, int index) {
		for (int i = index; i < index + data.length / 4 + data.length % 4; i ++) {
			memory [i] = ((int)(getData (data, i * 4)) << 24) + ((int)(getData (data, i * 4 + 1)) << 16) + ((int)(getData (data, i * 4 + 2)) << 8) + (int)getData (data, i * 4 + 3);
		}
		stack_pointer = index + data.length / 4;
		program_counter = index * 4;
	}
	public byte getData (byte[] data, int index) {
		if (index >= data.length) {
			return 0;
		} else {
			return data [index];
		}
	}
	public int readByte () {
		int shiftAmount = (3 - (program_counter % 4)) * 8;
		int readValue = (memory [program_counter / 4] & (0xFF << shiftAmount)) >> shiftAmount;
		program_counter ++;
		return readValue;
	}
	public int getLargest (int[] inputs) {
		int foo;
		int bar = inputs [0];
		for (int i = 0; i < inputs.length; i ++) {
			/*foo = bar - inputs [i];
			foo = -1 + (foo >>> 31);
			bar = (bar & foo) | (inputs [i] & (~foo));*/
			bar = (bar & (-1 + ((bar - inputs [i]) >>> 31))) | (inputs [i] & (-1 + ((inputs [i] - bar) >>> 31)));
		}
		return bar;
	}
	public void runInstruction () {
		if (running) {
			int opcode = readByte ();
			int opcodeID = opcode >>> 3;
			int argument;
			if (opcodeID != 0 && opcodeID != 1 && opcodeID != 8 && opcodeID != 9 && opcodeID != 10 && opcodeID != 11 && opcodeID != 12) {
				argument = (readByte () << 24) + (readByte () << 16) + (readByte () << 8) + readByte ();
			} else {
				argument = 0;
			}
			//System.out.println(opcode);
			runInstruction (opcode, argument);
		}
	}
	public void runInstruction (int opcode, int argument) {
		//OPCODES ARE 8-BIT, ARGUMENTS ARE 32-BIT
		/*
		    BRK
			END
			LDA
			LDX
			LSP
			STA
			STX
			SSP
			TAX
			TXA
			PSH
			PSA
			POP
			AND
			IOR
			XOR
			BSL
			BSR
			ADD
			SBC
			MLT
			DIV
			MOD
			CMP
			BEQ
			BNE
			BGT
			BLT
			JMP
			JSR
			RTS
			ITR
		 */
		byte opcodeId = (byte) ((opcode & 0xF8) >> 3);
		boolean relativeFlag = (opcode & 0x4) == 0x4;
		boolean miscFlag = (opcode & 0x2) == 0x2;
		boolean relXFlag = (opcode & 0x1) == 0x1;
		//System.out.println(opcodeId);
		if (relXFlag) {
			argument += x_register;
		}
		if (relativeFlag) {
			argument = memory [argument];
		}
		/*System.out.print(opcodeId);
		System.out.print(", ");
		System.out.println(argument);*/
		switch (opcodeId) {
			case 0:
				//BRK (break)
				break;
			case 1:
				//END (end)
				running = false;
				break;
			case 2:
				//LDA (load accumulator)
				accumulator = argument;
				break;
			case 3:
				//LDX (load x register)
				x_register = argument;
				break;
			case 4:
				//LSP (load stack pointer)
				stack_pointer = argument;
			case 5:
				//STA (store accumulator)
				memory [argument] = accumulator;
				break;
			case 6:
				//STX (store x register)
				memory [argument] = x_register;
				break;
			case 7:
				//SSP (store stack pointer)
				memory [argument] = stack_pointer;
				break;
			case 8:
				//TAX (transfer accumulator to x register) - No arguments
				x_register = accumulator;
				break;
			case 9:
				//TXA (transfer x register to accumulator) - No arguments
				accumulator = x_register;
				break;
			case 10:
				//PSH (push accumulator to stack) - No arguments
				stack_pointer ++;
				memory [stack_pointer] = accumulator;
				break;
			case 11:
				//PSA (pop stack to accumulator) - No arguments
				accumulator = memory [stack_pointer];
				stack_pointer --;
				break;
			case 12:
				//POP (pop stack) - No arguments
				stack_pointer --;
				break;
			case 13:
				//AND (bitwise and on accumulator)
				accumulator = accumulator & argument;
				break;
			case 14:
				//IOR (inclusive bitwise or on accumulator)
				accumulator = accumulator | argument;
				break;
			case 15:
				//XOR (exclusive bitwise or on accumulator)
				accumulator = accumulator ^ argument;
				break;
			case 16:
				//BSL (bit shift accumulator left)
				accumulator = accumulator << argument;
				break;
			case 17:
				//BSR (bit shift accumulator right)
				accumulator = accumulator >> argument;
				break;
			case 18:
				//ADD (add to accumulator)
				accumulator += argument;
				break;
			case 19:
				//SBC (subtract from accumulator)
				accumulator -= argument;
				break;
			case 20:
				//MLT (multiply accumulator)
				accumulator *= argument;
				break;
			case 21:
				//DIV (divide accumulator)
				accumulator /= argument;
				break;
			case 22:
				//MOD (modulo accumulator)
				accumulator %= argument;
				break;
			case 23:
				//CMP (compare accumulator to argument)
				if (accumulator == argument) {
					cmp_result = 1;
				} else if (argument > accumulator) {
					cmp_result = 2;
				} else if (argument < accumulator) {
					cmp_result = 3;
				}
				break;
			case 24:
				//BEQ (branch if last compared values were equal)
				if (cmp_result == 1) {
					program_counter = argument;
				}
				break;
			case 25:
				//BNE (branch if last compared values were not equal)
				if (cmp_result == 2 || cmp_result == 3) {
					program_counter = argument;
				}
				break;
			case 26:
				//BGT (branch if the argument was greater than the accumulator at the last cmp)
				if (cmp_result == 2) {
					program_counter = argument;
				}
				break;
			case 27:
				//BLT (branch if the argument was less than the accumulator at the last cmp)
				if (cmp_result == 3) {
					program_counter = argument;
				}
				break;
			case 28:
				//JMP (jump)
				program_counter = argument;
				break;
			case 29:
				//JSR (jump to subroutine - PUSHES THE RETURN ADDRESS TO THE TOP OF THE STACK)
				stack_pointer ++;
				memory [stack_pointer] = program_counter;
				break;
			case 30:
				//RTS (return from subroutine - USES TOP VALUE ON THE STACK FOR THE RETURN ADDRESS)
				program_counter = memory [stack_pointer];
				stack_pointer --;
				break;
			case 31:
				//ITR (interrupt)
				break;
		}
	}
}