const crypto = require('crypto');
const fs = require('fs');

// Load the private key from the file
const privateKey = fs.readFileSync('private_key.pem', { encoding: 'utf-8' });

// Function to decrypt data
function decrypt(encryptedData) {
  const buffer = Buffer.from(encryptedData, 'base64');
  const decrypted = crypto.privateDecrypt(
    {
      key: privateKey,
      padding: crypto.constants.RSA_PKCS1_PADDING
    },
    buffer,
  );
  return decrypted.toString('utf8');
}

// Example usage
const encryptedData = 'YwnkmyjymICNtB0beyrcU2Jiqg/O8ordCzr4e5SnEmS85j6RTE5Jul6QXmGkIxg5mVg76wFBw2SNcQ7wVY+klY2b2I5Kk5mb01+3WmFTCQxCFhlTgUsjxxOznBs4NCj/rbgZf/v7THm7WQgN/7V1thAJjoDyhij/wmgGhRLifvIqXIl4uomEMoSwqvwuM9K+5AY1TzmcCwZPfaGiID8mRTrWhB2ouZtrkVKH0D6Xvr2/syIPkisgn+Lm3D/xLui4A3kyy55eAwOOWo1u8L8Pg6U8KXiw10G0apfeei6kf4B94FejJrhDUTvaWhUNQYL8cJtKVhD2tuQjJT0MLP4RaQ==' // Encrypted data from the client (Base64 encoded)
const decryptedData = decrypt(encryptedData);
console.log('Decrypted Data:', decryptedData);
