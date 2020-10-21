const blacklist = require('metro-config/src/defaults/blacklist');

module.exports = {
  server: {
    port: 8088,
  },
  resolver: {
    blacklistRE: blacklist([/proj\.(android|ios_mac|linux|win32).*/])
  }
};
