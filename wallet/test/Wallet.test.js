const Wallet = artifacts.require('./Wallet.sol')

contract('Wallet', (accounts) => {
  let wallet

  before(async () => {
    wallet = await Wallet.deployed()
  })

  describe('deployment', async () => {
    it('deploys successfully', async () => {
      const address = await wallet.address
      assert.notEqual(address, 0x0)
      assert.notEqual(address, '')
      assert.notEqual(address, null)
      assert.notEqual(address, undefined)
    })

    it('has a name', async () => {
      const name = await wallet.name()
      assert.equal(name, 'Wallet')
    })

  })
})
