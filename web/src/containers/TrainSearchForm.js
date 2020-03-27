import React,{Component} from "react";
import {connect} from "react-redux";
import {updateDepartureStation,updateArrivalStation,updateJourneyDate} from "../store/actions/updateArrivalDepartureStations";
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
        this.handleDropDownItemClick = this.handleDropDownItemClick.bind(this);
        this.handleFormSubmit = this.handleFormSubmit.bind(this);
    }

    handleFocusChanges(e){
        let focus = -1;
        if(e.target.name=="from")
            focus=0;
        else if(e.target.name=="to")
            focus=1;
        this.setState({
            focus: focus
        });
    }

    handleDropDownItemClick(e){
        e.preventDefault();
        e.stopPropagation();
        let val = e.currentTarget.innerText.split("\n");
        if(this.state.focus == 0){
            let station = `${val[1]} - ${val[0]}`;
            this.props.dispatch(updateDepartureStation(station));
        }
        else if(this.state.focus == 1){
            let station = `${val[1]} - ${val[0]}`;
            this.props.dispatch(updateArrivalStation(station));
        }
        this.setState({
            focus: -1
        });
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
            let filteredData = matchedData.map((item,index)=> <SearchDropdownItem key={index}>{item.code}{item.name}{this.handleDropDownItemClick}</SearchDropdownItem>)
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
            let filteredData = matchedData.map((item,index)=> <SearchDropdownItem key={index}>{item.code}{item.name}{this.handleDropDownItemClick}</SearchDropdownItem>)
            this.setState({
                matchedToItems: filteredData,
            });
        }
        if(e.target.name == "date"){
            let d = new Date(e.target.value);
            this.props.dispatch(updateJourneyDate(d));
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

    handleFormSubmit(e){
        e.preventDefault();
        console.log("HERe");
        let url = "http://localhost:5000/trains/GKP/LKO/MON/SL";
        fetch(url).then((res)=>{
            if(!res.ok)
                throw("Error");
            return res.json()
        }).then((res)=>console.log(res)).catch((e)=>console.log(e));
    }

    render(){
        let date="";
        if(this.props.doj.getMonth()<9)
            date = `${this.props.doj.getFullYear()}-0${this.props.doj.getMonth()+1}-${this.props.doj.getDate()}`;
        else
            date = `${this.props.doj.getFullYear()}-${this.props.doj.getMonth()+1}-${this.props.doj.getDate()}`;
        return (
            <div className="train-search-form">
                <form className="search-form" onSubmit={this.handleFormSubmit}>
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
                        <input type="date" id="date" name="date" autoComplete="new-password"  value={date} onChange = {this.handleFormChanges} min ={date}></input>
                    </div>
                    <div className="search-button" onClick = {this.handleFormSubmit}>
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
        from_station: state.updateArrivalDepartureStations.from_station,
        doj : state.updateArrivalDepartureStations.doj
    }
}

export default connect(mapStateToProps,null)(TrainSearchForm);