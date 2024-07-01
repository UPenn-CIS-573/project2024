const fs = require('fs');
const crypto = require('crypto');

// Generate keys (if not already generated)
const { publicKey, privateKey } = crypto.generateKeyPairSync('rsa', {
  modulusLength: 2048,
  publicKeyEncoding: {
    type: 'spki',
    format: 'pem'
  },
  privateKeyEncoding: {
    type: 'pkcs8',
    format: 'pem'
  }
});

// Save the private key to a file
fs.writeFileSync('private_key.pem', privateKey, { encoding: 'utf-8' });
console.log('Private key saved to private_key.pem');

// Save the public key to a file (optional)
fs.writeFileSync('public_key.pem', publicKey, { encoding: 'utf-8' });
console.log('Public key saved to public_key.pem');

// encrypt data function
function encrypt(data) {
    const buffer = Buffer.from(data, 'utf8');
    const encrypted = crypto.publicEncrypt(
      {
        key: publicKey,
        padding: crypto.constants.RSA_PKCS1_PADDING
      },
      buffer,
    );
    return encrypted.toString('base64');
}

// Decrypt data function
function decrypt(encryptedData) {
    const buffer = Buffer.from(encryptedData, 'base64');
    const decrypted = crypto.privateDecrypt(
      {
        key: privateKey,
        padding: crypto.constants.RSA_PKCS1_PADDING,
      },
      buffer,
    );
    return decrypted.toString('utf8');
  }

let encrypt_data = encrypt('123456');
console.log(publicKey);

let decrypt_data = decrypt(encrypt_data);
console.log(decrypt_data);


