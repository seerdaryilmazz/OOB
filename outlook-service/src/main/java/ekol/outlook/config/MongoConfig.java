package ekol.outlook.config;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bol.crypt.CryptVault;
import com.bol.secure.CachedEncryptionEventListener;

@Configuration
public class MongoConfig {

	@Value("${outlook.old-key:MzViNDY4ZTY1ODE0NGY5Y2I1YjI4N2UwOTNhMDUzNDg=}")
	private String oldKey;

	@Value("${outlook.secret-key:NjdmYmRlYmFiMDZjNDYzYzlkMmZmMWIzNDIwYTBjYTE=}")
	private String secretKey;

	@Bean
	public CryptVault cryptVault() {
		return new CryptVault()
				.with256BitAesCbcPkcs5PaddingAnd128BitSaltKey(0, Base64.getDecoder().decode(oldKey))
				.with256BitAesCbcPkcs5PaddingAnd128BitSaltKey(1, Base64.getDecoder().decode(secretKey))
				.withDefaultKeyVersion(1);
	}

	@Bean
	public CachedEncryptionEventListener encryptionEventListener(CryptVault cryptVault) {
		return new CachedEncryptionEventListener(cryptVault);
	}

}
