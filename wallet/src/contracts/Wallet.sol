pragma solidity ^0.5.0;

contract Wallet{
  string public name;

  struct user{
    uint id;
    uint amount;
  }

  constructor() public{
    name = "Wallet";
  }
}
