package asm;

import java.util.ArrayList;

public abstract class Assembler {
	public static byte[] Compile (String data) {
		String[] opcodes = new String[] {"BRK", "END", "LDA", "LDX", "LSP", "STA", "STX", "SSP", "TAX", "TXA", "PSH", "PSA", "POP", "AND", "IOR", "XOR", "BSL", "BSR", "ADD", "SBC", "MLT", "DIV", "MOD", "CMP", "BEQ", "BNE", "BGT", "BLT", "JMP", "JSR", "RTS", "ITR"};
		String[] instructions = data.split ("\\r?\\n");
		if (data.equals ("")) {
			instructions = new String[0];
		}
		ArrayList<String> branchPoints = new ArrayList<String> ();
		ArrayList<Integer> branchPtrs = new ArrayList<Integer> ();
		ArrayList<Byte> outData = new ArrayList<Byte> ();
		int workingPC = 0;
		for (int i = 0; i < instructions.length; i ++) {
			if (instructions [i].charAt (0) != '@') {
				workingPC += getInstructionLength (instructions [i].split (" ")[0]);
			} else {
				branchPoints.add (instructions [i]);
				branchPtrs.add (new Integer (workingPC));
			}
		}
		for (int i = 0; i < instructions.length; i ++) {
			if (instructions [i].charAt (0) != '@') {
				String opcode = instructions [i].split (" ")[0];
				String flags = "";
				String argument = "";
				if (instructions [i].contains (",")) {
					flags = instructions [i].split (" ")[1].split (",")[0];
					if (getInstructionLength (opcode) != 1) {
						argument = instructions [i].split (" ")[1].split (",")[1];
					}
				} else {
					flags = "";
					if (getInstructionLength (opcode) != 1) {
						argument = instructions [i].split (" ")[1];
					}
				}
				Byte opcodeByte = (byte)(getInstructionID (opcode) << 3);
				if (flags.contains ("$")) {
					opcodeByte = (byte)(opcodeByte | 0x4);
				}
				if (flags.contains ("x")) {
					opcodeByte = (byte)(opcodeByte | 0x1);
				}
				if (opcodeByte != -1) {
					outData.add (opcodeByte);
				}
				if (getInstructionLength (opcode) != 1) {
					int workingArgument;
					if (isBranchInstruction (opcode)) {
						if (argument.charAt (0) == '@') {
							workingArgument = branchPtrs.get (branchPoints.indexOf (argument)).intValue ();
						} else {
							workingArgument = Integer.parseInt (argument);
						}
					} else {
						workingArgument = Integer.parseInt (argument);
					}
					outData.add (new Byte ((byte)((workingArgument & 0xFF000000) >> 24)));
					outData.add (new Byte ((byte)((workingArgument & 0x00FF0000) >> 16)));
					outData.add (new Byte ((byte)((workingArgument & 0x0000FF00) >> 8)));
					outData.add (new Byte ((byte)(workingArgument & 0x000000FF)));
				}
			}
		}
		byte[] outputArray = new byte[outData.size ()];
		for (int i = 0; i < outputArray.length; i ++) {
			outputArray [i] = outData.get (i).byteValue ();
		}
		return outputArray;
	}
	private static int getInstructionLength (String instruction) {
		if (instruction.equals ("NOP") || instruction.equals ("END") || instruction.equals ("TAX") || instruction.equals ("TXA") || instruction.equals ("PSH") || instruction.equals ("PSA") || instruction.equals ("POP")) {
			return 1;
		} else {
			return 5;
		}
	}
	private static int getInstructionID (String instruction) {
		String[] opcodes = new String[] {"BRK", "END", "LDA", "LDX", "LSP", "STA", "STX", "SSP", "TAX", "TXA", "PSH", "PSA", "POP", "AND", "IOR", "XOR", "BSL", "BSR", "ADD", "SBC", "MLT", "DIV", "MOD", "CMP", "BEQ", "BNE", "BGT", "BLT", "JMP", "JSR", "RTS", "ITR"};
		for (int i = 0; i < opcodes.length; i ++) {
			if (instruction.equals (opcodes [i])) {
				return i;
			}
		}
		return -1;
	}
	private static boolean isBranchInstruction (String instruction) {
		if (instruction.equals ("BEQ") || instruction.equals ("BNE") || instruction.equals ("BGT") || instruction.equals ("BLT") || instruction.equals ("JMP")) {
			return true;
		} else {
			return false;
		}
	}
}