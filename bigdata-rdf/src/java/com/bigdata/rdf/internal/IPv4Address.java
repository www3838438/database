/**

Copyright (C) SYSTAP, LLC 2006-2015.  All rights reserved.

Contact:
     SYSTAP, LLC
     2501 Calvert ST NW #106
     Washington, DC 20008
     licenses@systap.com

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; version 2 of the License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.bigdata.rdf.internal;

import java.util.Arrays;

import com.bigdata.btree.BytesUtil.UnsignedByteArrayComparator;

public class IPv4Address {

	final byte[] address;

	public IPv4Address(byte[] address) {
		this.address = address;
	}
	
	public IPv4Address(IPv4Address ip) {
		this.address = ip.getBytes();
	}

	/**
	 * Returns the byte array representation of the address
	 * 
	 * @return
	 */
	public byte[] getBytes() {
		return this.address;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(address);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IPv4Address other = (IPv4Address) obj;

		return UnsignedByteArrayComparator.INSTANCE.compare(address,
				other.address) == 0;
	}
	
	/**
	 * Return printable version of the IP address
	 */
	public String toString() {
		return byteArrayToIPString(address);
		
	}
	
	/**
	 * Converts the byte array to an IP string with option CIDR representation
	 * @param addr
	 * @return
	 */
	public static String byteArrayToIPString(byte [] addr) {
		final int netmask = addr[4] & 0xff;
    	
        return (addr[0] & 0xff) + "." + (addr[1] & 0xff) + "." + 
        	   (addr[2] & 0xff) + "." + (addr[3] & 0xff) +
        	   (netmask <= 32 ? "/" + netmask : ""); 
	}
	
	public static IPv4Address IPv4Factory(String... s) {

		byte[] address = new byte[5];
		long longVal;

		if(s.length == 4) {
		//Array of strings for IP address without CIDR
		//192.168.1.100
				for (int i = 0; i < 4; i++) {
					longVal = Integer.parseInt(s[i]);
					if (longVal < 0 || longVal > 0xff)
						return null;
					address[i] = (byte) (longVal & 0xff);
				}
				address[4] = (byte) (33 & 0xff);
		} else if (s.length == 5) {
		//Array of strings with CIDR
		//192.168.1.100/32

				for (int i = 0; i < 5; i++) {
					longVal = Integer.parseInt(s[i]);
					if (longVal < 0 || longVal > 0xff)
						return null;
					if (i == 4 && longVal > 32)
					    return null;
					address[i] = (byte) (longVal & 0xff);
				}
		} else {
			//Factory is undefined for other length string arrays
			return null;
		}

		return new IPv4Address(address);
	}

}