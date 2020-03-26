import {combineReducers} from "redux"; 
import initialAppData from "./initialAppData";
import updateArrivalDepartureStations from "./updateArrivalDepartureStations"

const rootReducer = combineReducers({
    initialAppData,
    updateArrivalDepartureStations
});

export default rootReducer;