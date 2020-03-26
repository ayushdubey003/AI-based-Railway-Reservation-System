import {combineReducers} from "redux"; 
import initialAppData from "./initialAppData";
import updateArrivalDepartureStations from "./updateArrivalDepartureStations"
import activeContentIndex from "./activeContentIndex";

const rootReducer = combineReducers({
    initialAppData,
    updateArrivalDepartureStations,
    activeContentIndex
});

export default rootReducer;