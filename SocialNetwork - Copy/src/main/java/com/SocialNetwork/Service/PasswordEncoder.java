<<<<<<< HEAD
package com.SocialNetwork.Service;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordEncoder {
	public String passwordEncoder(String password) {
		String result = "";
		result = BCrypt.withDefaults().hashToString(12, password.toCharArray()).toString();
		return result;
	}

	public boolean checkPassword(String passwordHash, String userInput) {
		if (passwordHash == null || passwordHash.isEmpty()) {
			return false;
		}

		return BCrypt.verifyer().verify(userInput.toCharArray(), passwordHash).verified;
	}
}
=======
package com.SocialNetwork.Service;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordEncoder {
	public String passwordEncoder(String password) {
		String result = "";
		result = BCrypt.withDefaults().hashToString(12, password.toCharArray()).toString();
		return result;
	}

	public boolean checkPassword(String passwordHash, String userInput) {
		if (passwordHash == null || passwordHash.isEmpty()) {
			return false;
		}

		return BCrypt.verifyer().verify(userInput.toCharArray(), passwordHash).verified;
	}
}
>>>>>>> b294e1654464e6366dd5c35e19e7cb01c33c5ba5
