import React,{Component} from 'react';
import '../App.css';
import {connect} from "react-redux";
import {loadInitialData} from "../store/actions/initialAppData";
import {TRAINS_LIST_LOADED,STATIONS_LIST_LOADED} from "../store/actionTypes";
import Navbar from "./Navbar";

class App extends Component{
  constructor(props){
    super(props);
  }

  componentDidMount(){
    this.props.dispatch(loadInitialData("http://localhost:5000/list",STATIONS_LIST_LOADED,"GET",{}));
    this.props.dispatch(loadInitialData("http://localhost:5000/trains",TRAINS_LIST_LOADED,"GET",{}));
  }

  render(){
    if(this.props.stationsList !=undefined){
      localStorage.setItem("stationsList",JSON.stringify(this.props.stationsList));
      localStorage.setItem("trainsList",JSON.stringify(this.props.trainsList));
    }
    return (
      <div className="App">
        <Navbar></Navbar>
      </div>
    )
  }
}

function mapStateToProps(state){
  return {
    trainsList: state.initialAppData.trainsList,
    stationsList: state.initialAppData.stationsList
  }
}

export default connect(mapStateToProps,null)(App);