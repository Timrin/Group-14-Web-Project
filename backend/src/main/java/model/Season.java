package model;

public enum Season {
	WINTER {
		@Override
		public String toString() {
			return "Winter";
		}
	},
	SPRING {
		@Override
		public String toString() {
			return "Spring";
		}
	},
	SUMMER {
		@Override
		public String toString() {
			return "Summer";
		}
	},
	AUTUMN {
		@Override
		public String toString() {
			return "Autumn";
		}
	}
}
