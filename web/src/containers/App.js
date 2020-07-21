import React,{Component} from 'react';
import '../App.css';
import {connect} from "react-redux";
import {loadInitialData} from "../store/actions/initialAppData";
import {TRAINS_LIST_LOADED,STATIONS_LIST_LOADED} from "../store/actionTypes";
import Navbar from "./Navbar";
import TrainSearchForm from "./TrainSearchForm";
import Feature from "../components/Feature";
import Headers from "../components/Headers";
import {Switch,Route,Redirect,Link} from "react-router-dom";
import RunningStatusForm from './RunningStatusForm';
import TrainListItem from './TrainListItem';

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
    
    let trains=[];
    if(this.props.trains.trains!=undefined)
      trains = this.props.trains.trains.map((item,index)=><TrainListItem key={index}>{item}</TrainListItem>)

    return (
      <div className="App">
        <Navbar></Navbar>
        <div className="header-content">
          <Headers></Headers>
        </div>
        <div className="loader-container" style={this.props.loading?{opacity:1}:{opacity:0}}>
          <div className="loader"></div>
        </div>
        <div className="form-container">
          <Switch>
            <Route exact path="/">
              <TrainSearchForm></TrainSearchForm>
            </Route>
            <Route exact path="/search/seats">
              <TrainSearchForm></TrainSearchForm>
            </Route>
            <Route exact path="/search/runningstatus">
              <RunningStatusForm></RunningStatusForm>
            </Route>
          </Switch>
        </div>
        <div className="feature-container">
          <Feature></Feature>
        </div>
        <div className="train-list-container" style={this.props.loading?{opacity:0}:{opacity:1}}>
          {trains}
        </div>
      </div>
    )
  }
}

function mapStateToProps(state){
  return {
    trainsList: state.initialAppData.trainsList,
    stationsList: state.initialAppData.stationsList,
    loading: state.loadTrains.loading,
    trains: state.loadTrains.trains
  }
}

export default connect(mapStateToProps,null)(App);