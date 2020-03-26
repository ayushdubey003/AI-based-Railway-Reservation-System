import React,{Component} from "react";
import {connect} from "react-redux";
import {updateDepartureStation,updateArrivalStation} from "../store/actions/updateArrivalDepartureStations";
import KMPSearch from "../services/KMPStringmatch";
import SearchDropdownItem from "../components/SearchDropdownItem";

class TrainSearchForm extends Component{
    constructor(props){
        super(props);
        this.handleFormChanges = this.handleFormChanges.bind(this);
        this.flipSides = this.flipSides.bind(this);
        this.state = {
            matchedForItems: [],
            matchedToItems: [],
            focus: -1
        }
        this.handleFocusChanges = this.handleFocusChanges.bind(this);
    }

    handleFocusChanges(e){
        let focus = -1;
        if(e.target.name=="from")
            focus=0;
        else if(e.target.name=="to")
            focus=1;
        this.setState(
            {
                focus: focus
            }
        )
    }

    handleFormChanges(e)
    {
        let value = e.target.value;
        if(e.target.name == "from"){
            this.props.dispatch(updateDepartureStation(value));
            let matchedData = [];
            if(value==undefined||value==""||value.length===0){
                this.setState({
                    matchedForItems: [],
                });
                return;
            }
            let ind=0;
            while(matchedData.length<=10&&ind<this.props.stationsList.length){
                let station = this.props.stationsList[ind++];
                if(KMPSearch(value,station.code)||KMPSearch(value,station.name))
                    matchedData.push(station);
            }
            let filteredData = matchedData.map((item,index)=> <SearchDropdownItem key={index}>{item.code}{item.name}</SearchDropdownItem>)
            this.setState({
                matchedForItems: filteredData,
            });
        }
        if(e.target.name == "to"){
            this.props.dispatch(updateArrivalStation(value));
            let matchedData = [];
            if(value==undefined||value==""||value.length===0){
                this.setState({
                    matchedToItems: []
                });
                return;
            }
            let ind=0;
            while(matchedData.length<=10&&ind<this.props.stationsList.length){
                let station = this.props.stationsList[ind++];
                if(KMPSearch(value,station.code)||KMPSearch(value,station.name))
                    matchedData.push(station);
            }
            let filteredData = matchedData.map((item,index)=> <SearchDropdownItem key={index}>{item.code}{item.name}</SearchDropdownItem>)
            this.setState({
                matchedToItems: filteredData,
            });
        }
    }

    flipSides(){
        let from = this.props.from_station;
        let to =this.props.to_station;
        let matchedForItems = this.state.matchedForItems;
        let matchedToItems = this.state.matchedToItems;
        this.props.dispatch(updateDepartureStation(to));
        this.props.dispatch(updateArrivalStation(from));
        this.setState({
            matchedForItems: matchedToItems,
            matchedToItems: matchedForItems
        })
    }

    render(){
        return (
            <div className="train-search-form">
                <form className="search-form">
                    <div className="from" onChange={this.handleFormChanges} autoComplete="new-password" onFocus={this.handleFocusChanges}>
                        <label htmlFor="from">From</label>
                        <input type="text" id="from" name="from" placeholder="Source Station" autoComplete="new-password" value={this.props.from_station}></input>
                    </div>
                    <div className="flip-arrow">
                        <div className="bidirectional-arrow" onClick={this.flipSides}>
                            <i className="fa fa-long-arrow-left"></i>
                            <i className="fa fa-long-arrow-right"></i>
                        </div>
                        <div className="circle"  onClick={this.flipSides}></div>
                    </div>
                    <div className="to" onChange={this.handleFormChanges} autoComplete="new-password" onFocus={this.handleFocusChanges}>
                        <label htmlFor="to">To</label>
                        <input type="text" id="to" name="to" placeholder="Destination Station" autoComplete="new-password" value={this.props.to_station}></input>
                    </div>
                    <div className="doj" autoComplete="new-password">
                        <label htmlFor="date">Date of Journey</label>
                        <input type="date" id="date" name="date" autoComplete="new-password"></input>
                    </div>
                    <div className="search-button">
                        <i className="fa fa-search" aria-hidden="true"></i>
                        <p>Find Seat Availability</p>
                    </div>
                </form>
                <div className='dropdown-results'>
                    <div className={this.state.focus == 0?'from-dropdown':'from-hidden'}>
                        {this.state.matchedForItems}
                    </div>
                    <div className={this.state.focus == 1?'to-dropdown':'to-hidden'}>
                        {this.state.matchedToItems}
                    </div>
                </div>
            </div>
        )
    }
}

function mapStateToProps(state){
    return{
        stationsList: state.initialAppData.stationsList,
        to_station: state.updateArrivalDepartureStations.to_station,
        from_station: state.updateArrivalDepartureStations.from_station 
    }
}

export default connect(mapStateToProps,null)(TrainSearchForm);