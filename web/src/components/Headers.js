import React,{Component} from "react";
import {connect} from "react-redux";
import activeContentIndex from "../store/actions/activeContentIndex"
import {Link} from "react-router-dom";

class Headers extends Component{
    
    constructor(props){
        super(props);
        this.handleClicks = this.handleClicks.bind(this);
    }

    handleClicks(e){
        console.log(e.target);
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
            <Link to='/search/seats' onClick={this.handleClicks}>
                <div className="generic-header 0">
                    <h1 className="0">SEARCH TRAINS</h1>
                    <div className={this.props.index == "0"?"underline":"underline-hidden"}></div>
                </div>
            </Link>
            <Link to='/search/runningstatus' onClick={this.handleClicks}>
                <div className="generic-header 1">
                    <h1 className="1">RUNNING STATUS</h1>
                    <div className={this.props.index == "1"?"underline":"underline-hidden"}></div>
                </div>
            </Link>
            <Link to='/search/pnr' onClick={this.handleClicks}>
                <div className="generic-header 2">
                    <h1 className="2">PNR STATUS ENQUIRY</h1>
                    <div className={this.props.index == "2"?"underline":"underline-hidden"}></div>
                </div>
            </Link>
            <Link to='/search/passingby'>
                <div className="generic-header 3" onClick={this.handleClicks}>
                    <h1 className="3">GET ALL TRAINS FROM SOURCE</h1>
                    <div className={this.props.index == "3"?"underline":"underline-hidden"}></div>
                </div>
            </Link>
            <Link to='/search/livestation' onClick={this.handleClicks}>
                <div className="generic-header 4">
                    <h1 className="4">SEARCH BY STATION</h1>
                    <div className={this.props.index == "4"?"underline":"underline-hidden"}></div>
                </div>
            </Link>
            <div className="generic-header 5" style={{opacity: 0}}>
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