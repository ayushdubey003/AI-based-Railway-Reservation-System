import React,{Component} from "react";
import {connect} from "react-redux";

class TrainSearchForm extends Component{
    constructor(props){
        super(props);
    }

    render(){
        return (
            <div className="train-search-form">
                <form className="search-form">
                    <div className="from">
                        <label for="from">From</label>
                        <input type="text" id="from" name="from" placeholder="Source Station"></input>
                    </div>
                    <div className="flip-arrow">
                        <div className="bidirectional-arrow">
                            <i class="fa fa-long-arrow-left"></i>
                            <i class="fa fa-long-arrow-right"></i>
                        </div>
                        <div className="circle"></div>
                    </div>
                    <div className="to">
                        <label for="to">To</label>
                        <input type="text" id="to" name="to" placeholder="Destination Station"></input>
                    </div>
                    <div className="doj">
                        <label for="date">Date of Journey</label>
                        <input type="date" id="date" name="date"></input>
                    </div>
                    <div className="search-button">
                        <i class="fa fa-search" aria-hidden="true"></i>
                        <p>Find Seat Availability</p>
                    </div>
                </form>
            </div>
        )
    }
}

function mapStateToProps(state){
    return{
        stationsList: state.initialAppData.stationsList
    }
}

export default connect(mapStateToProps,null)(TrainSearchForm);