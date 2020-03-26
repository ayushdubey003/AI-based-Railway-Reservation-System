import React,{Component} from "react";
import {connect} from "react-redux";
import activeContentIndex from "../store/actions/activeContentIndex"

class Headers extends Component{
    
    constructor(props){
        super(props);
        this.handleClicks = this.handleClicks.bind(this);
    }

    handleClicks(e){
        switch(e.target.className){
            case "0":
                this.props.dispatch(activeContentIndex(0));
                return;
            case "1":
                this.props.dispatch(activeContentIndex(1));
                return;
            case "2":
                this.props.dispatch(activeContentIndex(2));
                return;
            case "3":
                this.props.dispatch(activeContentIndex(3));
                return;
            case "4":
                this.props.dispatch(activeContentIndex(4));
                return;
            default:
                return;
        }
    }

    render(){
        return <div className="header">
            <div onClick={this.handleClicks}>
                <h1 className="0">SEARCH TRAINS</h1>
                <div className={this.props.index == "0"?"underline":"underline-hidden"}></div>
            </div>
            <div onClick={this.handleClicks}>
                <h1 className="1">RUNNING STATUS</h1>
                <div className={this.props.index == "1"?"underline":"underline-hidden"}></div>
            </div>
            <div onClick={this.handleClicks}>
                <h1 className="2">PNR STATUS ENQUIRY</h1>
                <div className={this.props.index == "2"?"underline":"underline-hidden"}></div>
            </div>
            <div onClick={this.handleClicks}>
                <h1 className="3">SEARCH BY NAME/NUMBER</h1>
                <div className={this.props.index == "3"?"underline":"underline-hidden"}></div>
            </div>
            <div onClick={this.handleClicks}>
                <h1 className="4">SEARCH BY STATION</h1>
                <div className={this.props.index == "4"?"underline":"underline-hidden"}></div>
            </div>
            <div style={{opacity: 0}}>
                <h1>SEARCH BY STATIOSSSSSSN</h1>
                <div className="underline"></div>
            </div>
        </div>
    }
}

function mapStateToProps(state){
    return {
        index: state.activeContentIndex.index
    }
}

export default connect(mapStateToProps,null)(Headers);