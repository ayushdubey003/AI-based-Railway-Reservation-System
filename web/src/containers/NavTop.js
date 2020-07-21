import React,{Component} from "react";

export default class NavTop extends Component{

    constructor(props){
        super(props);
        this.state = {
            userToken: undefined
        }
    }

    signUpComponents(){
        return  <ul>
                    <li>Sign Up</li>
                    <li>Log In</li>
                </ul>
    }

    signedUpComponents(){
        return  <ul>
                    <li>My Wallet</li>
                    <li>Profile</li>
                </ul>
    }

    componentDidMount(){
        
    }

    render()
    {
        return (
            <div className="navtop">
                <div className="firstColumns">
                    <h1>Railway Reservation System</h1>
                </div>
                <div className="secondColumns">
                    {this.state.userToken === undefined? this.signUpComponents():this.signedUpComponents()}
                </div>
            </div>
        )
    }
}