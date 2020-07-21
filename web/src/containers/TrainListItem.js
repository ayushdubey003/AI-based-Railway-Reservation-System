import React,{Component} from "react";
import {connect} from "react-redux";

class TrainListItem extends Component{
    constructor(props){
        super(props);
    }

    render(){
        let train = this.props.children;
        return <div className="train-item">
            <div className="train-details">
                <div className="train-number">
                    {train['train no']}
                </div>
                <div className="train-name">
                    {train['train name']}
                    <span className="train-type">
                        ({train['type']})
                    </span>
                </div>
                <i className="fa fa-map-marker" aria-hidden="true"></i>
            </div>
            <div className="time-details">
                <div className="src-time">{train['departure times'][0]}</div>
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
                <div className="dept-time">{train['arrival times'][train['arrival times'].length-2]}</div>
            </div>
            <div className="card-navbar">
                <div className="six-days">View six days availability</div>
                <div className="six-days" style={{opacity: 0}}>Vieew hahaha</div>

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
                <div className="six-days" style={{opacity: 0}}>Vieew</div>
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