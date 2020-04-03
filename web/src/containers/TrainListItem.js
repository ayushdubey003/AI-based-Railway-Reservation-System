import React,{Component} from "react";
import {connect} from "react-redux";

class TrainListItem extends Component{
    constructor(props){
        super(props);
    }

    render(){
        return <div className="train-item">
            <div className="train-details">
                <div className="train-number">
                    15909
                </div>
                <div className="train-name">
                    abadh assam express
                    <span className="train-type">
                        (Mail/Express)
                    </span>
                </div>
                <i class="fa fa-map-marker" aria-hidden="true"></i>
            </div>
            <div className="time-details">
                <div className="src-time">03:20pm</div>
                <div className="middle">
                    <div className="duration">
                        <div className="view"></div>
                        06hr
                        <div className="view"></div>
                    </div>
                    <div className="running-days">
                            s m t w t f s
                    </div>
                </div>
                <div className="dept-time">05:45am</div>
            </div>
            <div className="card-navbar">
                <div className="six-days">View six days availability</div>
                <div className="ticket-details">
                    <div className="travel-class">
                        SL
                    </div>
                    <div className="fare">
                        ₹ 415
                    </div>
                    <div className="ticket-status">
                        RLWL53/WL27
                    </div>
                    <div className="confirmation-probability">
                        58%
                    </div>
                </div>
                <div className="four-months">View four months availability</div>
            </div>
        </div>
    }
}

function mapStateToProps(state){
    return {
        trains: state.loadTrains.trains
    }
}

export default connect(mapStateToProps,null)(TrainListItem);