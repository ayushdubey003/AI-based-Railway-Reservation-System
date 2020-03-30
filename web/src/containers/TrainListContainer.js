import React,{Component} from "react";
import {connect} from "react-redux";

class TrainListContainer extends Component{
    constructor(props){
        super(props);
    }

    render(){
        return <div className="train-list"></div>
    }
}

function mapStateToProps(state){
    return {
        trains: state.loadTrains.trains
    }
}

export default connect(mapStateToProps,null)(TrainListContainer);