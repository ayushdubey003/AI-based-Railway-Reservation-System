import {combineReducers} from "redux"; 
import initialAppData from "./initialAppData";
import updateArrivalDepartureStations from "./updateArrivalDepartureStations"
import activeContentIndex from "./activeContentIndex";
import loadTrains from "./loadTrains";

const rootReducer = combineReducers({
    initialAppData,
    updateArrivalDepartureStations,
    activeContentIndex,
    loadTrains
});

export default rootReducer;